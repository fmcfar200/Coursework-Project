package mpdproject.gcu.me.org.assignmenttest1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

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

        //dateButton = (Button) findViewById(R.id.datebutton);
        //dateButton.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);

        toolbar = (Toolbar)findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Bundle extras = getIntent().getExtras();
        int fetchType = extras.getInt("FetchType");;
        if (fetchType == 1)
        {
            startProgress(url1, fetchType);
        }
        else
        {
            startProgress(url3, fetchType);

        }


        theDialog = new ProgressDialog(ItemViewer.this);
        theDialog.setMessage("Getting Traffic Info...");
        theDialog.show();

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.viewer_menu,menu);
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
        return true;
    }



    public void startProgress(String url, int fetchType) {

        new Thread(new Task(url, fetchType)).start();


    }

    class Task extends AsyncTask implements Runnable {
        private String url;
        private int fetchType;


        public Task(String aurl, int afetchType) {
            url = aurl;
            fetchType = afetchType;
        }

        @Override
        public void run() {
            RoadWorksItem rwItem = null;
            rwList = new ArrayList<>();

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
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
                                } else if (rwItem != null) {
                                    if (pp.getName().equalsIgnoreCase("title"))
                                    {
                                        rwItem.title = pp.nextText().trim();
                                    } else if (pp.getName().equalsIgnoreCase("description"))
                                    {
                                        rwItem.desc = pp.nextText().trim();
                                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                                        if (fetchType == 2)
                                        {
                                            String[] sd = rwItem.desc.split(":");
                                            String[] sd2 = sd[1].split(", ");
                                            String[] sd3 = sd2[1].split(" - ");
                                            String startDate = sd3[0];

                                            String ed = sd[3];
                                            String[] ed2 = ed.split(", ");
                                            String[] ed3 = ed2[1].split(" - ");
                                            String endDate = ed3[0];

                                            Date myStartDate = df.parse(startDate);
                                            Date myEndDate = df.parse(endDate);

                                            rwItem.startDate = df.format(myStartDate);
                                            rwItem.endDate = df.format(myEndDate);
                                        }
                                        else
                                        {
                                            Date current = Calendar.getInstance().getTime();
                                            rwItem.startDate = df.format(current);
                                            rwItem.endDate = df.format(current);
                                        }



                                    } else if (pp.getName().equalsIgnoreCase("link"))
                                    {
                                        rwItem.link = pp.nextText().trim();
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
                    Log.e("UI thread", "I am the UI thread:  " + rwList.size());
                    /*
                    for (int i = 0; i < rwList.size(); i++)
                    {
                        if (fetchType == 1)
                        {
                            urlInput.setText(urlInput.getText() + rwList.get(i).title + "\n" + rwList.get(i).desc + "\n" + rwList.get(i).link + "\n \n");
                        }
                        else
                        {
                            urlInput.setText(urlInput.getText() + rwList.get(i).title + "\n" + rwList.get(i).startDate + "\n" + rwList.get(i).endDate + "\n" + rwList.get(i).link + "\n \n");

                            long dateDifference = rwList.get(i).endDate.getTime() - rwList.get(i).startDate.getTime();
                            long seconds = dateDifference / 1000;
                            long minutes = seconds/60;
                            long hours = minutes/60;
                            long days = hours/24;

                            if (days < 7)
                            {
                                urlInput.setTextColor(getResources().getColor(R.color.green));
                            }
                            else if (days > 7)
                            {

                                Log.e("Days: ", Long.toString(days));
                            }
                        }


                        //rwList.remove(i);

                    }
                    */
                    Log.d("My list: ", String.valueOf(rwList.size()));
                    adapter = new TrafficAdapter(getApplicationContext(),R.layout.listview_item_layout,rwList);
                    listView.setAdapter(adapter);


                    theDialog.dismiss();

                }

            });


        }
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }


    }

}
