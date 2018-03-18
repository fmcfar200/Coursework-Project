package mpdproject.gcu.me.org.assignmenttest1;

import java.util.Date;

/**
 * Created by Fraser on 23/02/2018.
 */

public class RoadWorksItem
{
    String title;
    String desc;
    String link;

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
}
