package mpdproject.gcu.me.org.assignmenttest1;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Fraser on 22/03/2018.
 */

public class RSSReader {

    private String theURL;
    private int fetchType;

    public RSSReader(String url, int fetchType)
    {
        theURL = url;
        fetchType = fetchType;
    }

    public List<RoadWorksItem> runReader()
        {
            RoadWorksItem rwItem = null;
             List<RoadWorksItem> rwList = new ArrayList<>();

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            try {
                aurl = new URL(this.theURL);
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

            return rwList;
        }
}
