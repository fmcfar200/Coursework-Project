package mpdproject.gcu.me.org.assignmenttest1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Fraser on 15/03/2018.
 */

public class TrafficAdapter extends ArrayAdapter<RoadWorksItem>
{
    Runnable context;
    int layoutId;
    List<RoadWorksItem> data = null;

    public TrafficAdapter(Runnable context, int layoutId, List<RoadWorksItem> data)
    {
        super((Context) context,layoutId,data);
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }

    static class TrafficHolder
    {
        TextView locationText;
        TextView dateText;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        View row = convertView;
        TrafficHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutId,parent,false);

            holder = new TrafficHolder();
            holder.locationText = (TextView)row.findViewById(R.id.locationText);
            holder.dateText = (TextView)row.findViewById(R.id.iddateText);

            row.setTag(holder);


        }
        else
        {
            holder = (TrafficHolder)row.getTag();
        }

        RoadWorksItem item = data.get(pos);
        holder.locationText.setText(item.title);
        holder.dateText.setText(item.startDate.toString() + "  " + item.endDate.toString());

        return row;

    }
}
