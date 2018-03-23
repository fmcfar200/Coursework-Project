package mpdproject.gcu.me.org.assignmenttest1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemViewer extends AppCompatActivity{


    private String url1 = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String url3 = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    private String theURL;

    private String result = "";

    Toolbar toolbar;

    List<RoadWorksItem> rwList = new ArrayList<>();
    TrafficAdapter adapter;


    Button dateButton;
    ListView listView;

    ProgressDialog theDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);


        listView = (ListView)findViewById(R.id.listView);

        toolbar = (Toolbar)findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        Bundle extras = getIntent().getExtras();
        int fetchType = extras.getInt("FetchType");;
        if (fetchType == 1)
        {
            BeginFeed(url1, fetchType);
            getSupportActionBar().setTitle("Current Incidents");
        }
        else
        {
            BeginFeed(url3, fetchType);
            getSupportActionBar().setTitle("Planned Roadworks");

        }


        dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void  onDateSet(DatePicker picker, int year, int month, int day)
            {
                month = month + 1;

                String dateString = Integer.toString(day)
                        + Integer.toString(month) + Integer.toString(year);

                SimpleDateFormat df = new SimpleDateFormat("ddMyyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("dd MMMM yyyy");



                try {
                    Date mySearchDate = df.parse(dateString);
                    String theSearchDate = df2.format(mySearchDate);

                    List<RoadWorksItem> newList = new ArrayList<RoadWorksItem>();
                    for (RoadWorksItem item: rwList)
                    {
                        if (item.startDate.equals(theSearchDate))
                        {
                            newList.add(item);
                        }
                    }

                    adapter = new TrafficAdapter(getApplicationContext(),R.layout.listview_item_layout,newList);
                    listView.setAdapter(adapter);

                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
            {
                RoadWorksItem item = (RoadWorksItem)adapter.getItemAtPosition(position);
                StartFullDetail(item);

            }
        });



    }

    private void StartFullDetail(RoadWorksItem item)
    {
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        i.putExtra("RoadWorksItem",item);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.viewer_menu,menu);

        final SearchView searchView = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.searchIcon));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.calendarIcon)
        {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dDialog = new DatePickerDialog(
                    this,
                    R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                    dateSetListener,
                    year,month,day);

            dDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            dDialog.show();
        }
        else if (id == R.id.searchIcon)
        {

        }
        return true;
    }



    public void BeginFeed(String url, int fetchType)
    {
        RSSFetch rssFetch = new RSSFetch(url, fetchType);
        rssFetch.execute();

    }

    class RSSFetch extends AsyncTask<Void, Void, List<RoadWorksItem>>{

        private String theUrl;
        private int theFetchType;
        List<RoadWorksItem> thelist;

        public RSSFetch(String url, int fetchType)
        {
            theUrl = url;
            theFetchType = fetchType;

        }

        @Override
        protected void onPreExecute()
        {
            theDialog = new ProgressDialog(ItemViewer.this);
            theDialog.setMessage("Getting Traffic Info...");
            theDialog.show();
        }

        @Override
        protected List<RoadWorksItem> doInBackground(Void... theVoid) {
            RoadWorksItem rwItem = null;
            rwList = new ArrayList<>();

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            try {
                aurl = new URL(theUrl);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception" + ae.getCause());
            }

            if (result != null) {
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser pp = factory.newPullParser();
                    pp.setInput(new StringReader(result));

                    int eventType = pp.getEventType();
                    boolean finished = false;
                    while (eventType != XmlPullParser.END_DOCUMENT && !finished) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:

                                if (pp.getName().equalsIgnoreCase("item")) {
                                    rwItem = new RoadWorksItem(" ", " ", " ");
                                } else if (rwItem != null) {
                                    if (pp.getName().equalsIgnoreCase("title")) {
                                        rwItem.title = pp.nextText().trim();
                                    } else if (pp.getName().equalsIgnoreCase("description")) {
                                        rwItem.desc = pp.nextText().trim();
                                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                                        if (theFetchType == 2) {
                                            //splits up by colons
                                            String[] sd = rwItem.desc.split(":");

                                            //split date for formatting
                                            String[] sd2 = sd[1].split(", ");
                                            String[] sd3 = sd2[1].split(" - ");
                                            String startDate = sd3[0];

                                            String ed = sd[3];
                                            String[] ed2 = ed.split(", ");
                                            String[] ed3 = ed2[1].split(" - ");
                                            String endDate = ed3[0];

                                            //parses dates
                                            Date myStartDate = df.parse(startDate);
                                            Date myEndDate = df.parse(endDate);

                                            //sets Date and String variables
                                            rwItem.sdDate = myStartDate;
                                            rwItem.edDate = myEndDate;
                                            rwItem.startDate = df.format(myStartDate);
                                            rwItem.endDate = df.format(myEndDate);


                                            String w = sd[5].replaceAll("Traffic Management", " ");
                                            String m = sd[6].replaceAll("Diversion Information", " ");
                                            rwItem.setWorks(w);
                                            rwItem.setManagement(m);

                                            if (rwItem.desc.contains("Diversion Information")) {
                                                String diversionString = rwItem.desc.substring(rwItem.desc.indexOf("Diversion Information:"));
                                                diversionString = diversionString.replaceAll("Diversion Information:", "");
                                                rwItem.setDiversionInfo(diversionString);
                                            } else {
                                                rwItem.setDiversionInfo(null);
                                            }


                                        } else {
                                            Date current = Calendar.getInstance().getTime();
                                            rwItem.edDate = current;
                                            rwItem.sdDate = current;
                                            rwItem.startDate = df.format(current);
                                            rwItem.endDate = df.format(current);
                                        }


                                    } else if (pp.getName().equalsIgnoreCase("link")) {
                                        rwItem.link = pp.nextText().trim();

                                    } else if (pp.getName().equalsIgnoreCase("point")) {
                                        String llStr = pp.nextText().trim();
                                        String lonString = llStr.substring(llStr.indexOf("-"));
                                        String latString = llStr.replaceAll(lonString, "");

                                        rwItem.setLat(Double.valueOf(latString));
                                        rwItem.setLon(Double.valueOf(lonString));


                                    }

                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if (pp.getName().equalsIgnoreCase("item") && rwItem != null) {
                                    rwList.add(rwItem);

                                } else if (pp.getName().equalsIgnoreCase("channel")) {
                                    finished = true;
                                }
                                break;
                        }
                        eventType = pp.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            return rwList;
        }



        @Override
        protected void onPostExecute(List<RoadWorksItem> result)
        {
            adapter = new TrafficAdapter(getApplicationContext(),R.layout.listview_item_layout,result);
            listView.setAdapter(adapter);
            theDialog.dismiss();
        }
    }

        /*

        @Override
        public void run() {
            RoadWorksItem rwItem = null;
            rwList = new ArrayList<>();

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception" + ae.getCause());
            }

            if (result != null)
            {
                try
                {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser pp = factory.newPullParser();
                    pp.setInput(new StringReader(result));

                    int eventType = pp.getEventType();
                    boolean finished = false;
                    while (eventType != XmlPullParser.END_DOCUMENT && !finished)
                    {
                        switch (eventType)
                        {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:

                                if (pp.getName().equalsIgnoreCase("item"))
                                {
                                    rwItem = new RoadWorksItem(" "," ", " ");
                                } else if (rwItem != null)
                                {
                                    if (pp.getName().equalsIgnoreCase("title"))
                                    {
                                        rwItem.title = pp.nextText().trim();
                                    }
                                    else if (pp.getName().equalsIgnoreCase("description"))
                                    {
                                        rwItem.desc = pp.nextText().trim();
                                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                                        if (fetchType == 2)
                                        {
                                            //splits up by colons
                                            String[] sd = rwItem.desc.split(":");

                                            //split date for formatting
                                            String[] sd2 = sd[1].split(", ");
                                            String[] sd3 = sd2[1].split(" - ");
                                            String startDate = sd3[0];

                                            String ed = sd[3];
                                            String[] ed2 = ed.split(", ");
                                            String[] ed3 = ed2[1].split(" - ");
                                            String endDate = ed3[0];

                                            //parses dates
                                            Date myStartDate = df.parse(startDate);
                                            Date myEndDate = df.parse(endDate);

                                            //sets Date and String variables
                                            rwItem.sdDate = myStartDate;
                                            rwItem.edDate = myEndDate;
                                            rwItem.startDate = df.format(myStartDate);
                                            rwItem.endDate = df.format(myEndDate);


                                            String w = sd[5].replaceAll("Traffic Management"," ");
                                            String m = sd[6].replaceAll("Diversion Information"," ");
                                            rwItem.setWorks(w);
                                            rwItem.setManagement(m);

                                            if (rwItem.desc.contains("Diversion Information"))
                                            {
                                                String diversionString = rwItem.desc.substring(rwItem.desc.indexOf("Diversion Information:"));
                                                diversionString = diversionString.replaceAll("Diversion Information:","");
                                                rwItem.setDiversionInfo(diversionString);
                                            }
                                            else
                                            {
                                                rwItem.setDiversionInfo(null);
                                            }



                                        }
                                        else
                                        {
                                            Date current = Calendar.getInstance().getTime();
                                            rwItem.edDate = current;
                                            rwItem.sdDate = current;
                                            rwItem.startDate = df.format(current);
                                            rwItem.endDate = df.format(current);
                                        }



                                    }
                                    else if (pp.getName().equalsIgnoreCase("link"))
                                    {
                                        rwItem.link = pp.nextText().trim();

                                    }
                                    else if (pp.getName().equalsIgnoreCase("point"))
                                    {
                                        String llStr = pp.nextText().trim();
                                        String lonString = llStr.substring(llStr.indexOf("-"));
                                        String latString = llStr.replaceAll(lonString,"");

                                        rwItem.setLat(Double.valueOf(latString));
                                        rwItem.setLon(Double.valueOf(lonString));


                                    }

                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if (pp.getName().equalsIgnoreCase("item") && rwItem != null)
                                {
                                    rwList.add(rwItem);

                                } else if (pp.getName().equalsIgnoreCase("channel"))
                                {
                                    finished = true;
                                }
                                break;
                        }
                        eventType = pp.next();
                    }
                }
                catch (XmlPullParserException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            ItemViewer.this.runOnUiThread(new Runnable() {
                public void run() {


                }

            });




        }

        */


}
