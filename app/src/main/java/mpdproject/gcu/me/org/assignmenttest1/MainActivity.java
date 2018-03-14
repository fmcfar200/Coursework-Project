//
//
// Starter code for the Mobile Platform Development Assignment
// Seesion 2017/2018
//
//

package mpdproject.gcu.me.org.assignmenttest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{


    private Button incidentsButton;
    private Button plannedRWButton;




    private enum fetchType
    {
        ROADWORKS,
        INCIDENTS,
        PLANNED_RW
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incidentsButton = (Button)findViewById(R.id.incidentsButton);
        incidentsButton.setOnClickListener(this);

        plannedRWButton = (Button)findViewById(R.id.plannedRWButton);
        plannedRWButton.setOnClickListener(this);

    } // End of onCreate

    public void onClick(View aview)
    {
        if (aview == incidentsButton)
        {
            StartIncidents();
        }
        else if (aview == plannedRWButton)
        {
            StartPlannedRW();
        }

    }

    public void StartIncidents()
    {
        Intent i = new Intent(getApplicationContext(), ItemViewer.class);
        i.putExtra("FetchType", 1);
        startActivity(i);
    }

    public void StartPlannedRW()
    {
        Intent i = new Intent(getApplicationContext(), ItemViewer.class);
        i.putExtra("FetchType", 2);
        startActivity(i);
    }


    /*
    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
    private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
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
                                        rwItem = new RoadWorksItem();
                                    } else if (rwItem != null) {
                                        if (pp.getName().equalsIgnoreCase("title")) {
                                            rwItem.title = pp.nextText().trim();
                                            Log.e("Attrib", rwItem.title);
                                        } else if (pp.getName().equalsIgnoreCase("description")) {
                                            rwItem.desc = pp.nextText().trim();
                                            Log.e("Attrib", rwItem.desc);
                                            rwItem.desc.replace("<", "");
                                            rwItem.desc.replace(">", "");
                                            rwItem.desc.replace("/", "");


                                        } else if (pp.getName().equalsIgnoreCase("link")) {
                                            rwItem.link = pp.nextText().trim();
                                            Log.e("Attrib", rwItem.link);
                                        }

                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (pp.getName().equalsIgnoreCase("item") && rwItem != null) {
                                        rwList.add(rwItem);
                                        Log.e("Action", "add");

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
                    }

                }
                // Now update the TextView to display raw XML data
                // Probably not the best way to update TextView
                // but we are just getting started !

                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("UI thread", "I am the UI thread:  " + rwList.size());

                        for (int i = 0; i < rwList.size(); i++) {
                            urlInput.setText(urlInput.getText() + rwList.get(i).title + "\n" + rwList.get(i).desc + "\n" + rwList.get(i).link + "\n \n");
                            rwList.remove(i);

                        }

                        Log.d("My list: ", String.valueOf(rwList.size()));
                        //urlInput.setText(result);
                    }

                });


        }

    }

*/
}
