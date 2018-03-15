package mpdproject.gcu.me.org.assignmenttest1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemViewer extends AppCompatActivity implements View.OnClickListener {


    private String url1 = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String url3 = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    private String theURL;

    private TextView urlInput;
    private String result = "";
    boolean fetched = false;

    List<RoadWorksItem> rwList = new ArrayList<>();
    ListView listView;
    TrafficAdapter adapter;

    List<RoadWorksItem> items;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);

        urlInput = (TextView)findViewById(R.id.urlInput);
        listView = (ListView)findViewById(R.id.listView);

        Bundle extras = getIntent().getExtras();
        int fetchType = extras.getInt("FetchType");;
        Log.e("tag", Integer.toString(fetchType));

        items = new ArrayList<RoadWorksItem>();

        items.add(new RoadWorksItem("hello", "hello", "hello"));
        items.add(new RoadWorksItem("meehhh", "hello", "hello"));



        // adapter = new TrafficAdapter(this,R.layout.listview_item_layout,items);

        //listView.setAdapter(adapter);


        if (fetchType == 1)
        {
            startProgress(url1, fetchType);
        }
        else
        {
            startProgress(url3, fetchType);

        }



        /*
        if (fetchType != null)
        {
            if (fetchType == "Current")
            {
                Log.e("type", fetchType);
                //startProgress(url1);

            }
            else if (fetchType == "Planned")
            {
                //startProgress(url3);
            }
        }

        */
    }


    public void onClick(View v)
    {

    }

    protected  void onStart()
    {
        super.onStart();


    }


    public void startProgress(String url, int fetchType) {

        new Thread(new Task(url, fetchType)).start();

        // Run network access on a separate thread;

    } //

    class Task implements Runnable {
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
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    //Log.e("MyTag",inputLine);

                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //
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

                                            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                                            Date myStartDate = df.parse(startDate);
                                            Date myEndDate = df.parse(endDate);

                                            Log.e("Start Date", String.valueOf(myStartDate));
                                            Log.e("End Date", String.valueOf(myEndDate));


                                            rwItem.startDate = myStartDate;
                                            rwItem.endDate = myEndDate;
                                        }
                                        else
                                        {
                                            rwItem.startDate = null;
                                            rwItem.endDate = null;
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
            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

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


                    //urlInput.setText(result);
                }

            });


        }
    }

}
