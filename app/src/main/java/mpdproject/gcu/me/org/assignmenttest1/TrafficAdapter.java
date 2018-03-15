package mpdproject.gcu.me.org.assignmenttest1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Fraser on 15/03/2018.
 */

public class TrafficAdapter extends BaseAdapter
{
    Context context;
    int layoutId;
    List<RoadWorksItem> data;

    public TrafficAdapter( Context context, int layoutId, List<RoadWorksItem> data)
    {
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, layoutId,null);
        TextView locationText = (TextView)v.findViewById(R.id.locationText);
        TextView date = (TextView)v.findViewById(R.id.iddateText);

        locationText.setText(data.get(position).title);
        date.setText(data.get(position).startDate.toString() + " - " + data.get(position).endDate.toString() );

        v.setTag(data.get(position));

        return v;
    }


}
