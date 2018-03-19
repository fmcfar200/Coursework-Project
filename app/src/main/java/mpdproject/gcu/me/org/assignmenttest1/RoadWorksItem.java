package mpdproject.gcu.me.org.assignmenttest1;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fraser on 23/02/2018.
 */

public class RoadWorksItem implements Serializable
{
    String title;
    String desc;
    String link;

    String works;
    String management;
    String diversionInfo;

    String startDate;
    String endDate;
    Date sdDate;
    Date edDate;

    public RoadWorksItem(String title, String desc, String link)
    {
        this.title = title;
        this.desc = desc;
        this.link = link;
    }

    public long getLengthOfRW()
    {
        if (startDate != null && endDate != null)
        {
            long dateDifference = this.edDate.getTime() - this.sdDate.getTime();
            long seconds = dateDifference / 1000;
            long minutes = seconds/60;
            long hours = minutes/60;
            long days = hours/24;

            return days;
        }
        else
        {
            return 0;
        }
    }

    public void setWorks(String works)
    {
        this.works = works;
    }
    public void setManagement(String management)
    {
        this.management = management;
    }
    public void setDiversionInfo(String diversionInfo)
    {
        this.diversionInfo = diversionInfo;
    }

    public String getWorks()
    {
        return this.works;
    }
    public String getManagement()
    {
        return this.management;
    }
    public String getDiversionInfo()
    {
        return this.diversionInfo;
    }

}
