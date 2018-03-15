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

    Date startDate;
    Date endDate;

    public RoadWorksItem(String title, String desc, String link)
    {
        this.title = title;
        this.desc = desc;
        this.link = link;
    }
}
