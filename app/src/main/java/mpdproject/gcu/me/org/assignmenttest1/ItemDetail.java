package mpdproject.gcu.me.org.assignmenttest1;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class ItemDetail extends FragmentActivity implements OnMapReadyCallback {

    private TextView titleText;
    private TextView descriptionText;

    GoogleMap mGMap;
    MapView mapView;

    RoadWorksItem theItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        titleText = (TextView)findViewById(R.id.titleID);
        descriptionText = (TextView)findViewById(R.id.descriptionID);
        mapView = (MapView)findViewById(R.id.map);

        theItem = (RoadWorksItem) getIntent().getSerializableExtra("RoadWorksItem");

        titleText.setText(theItem.title);

        if (theItem.getWorks() != null)
        {
            descriptionText.setText("Works: " + theItem.getWorks() +"\n"
                    + "Traffic Management: " + theItem.getManagement() + "\n"
                    + "Link: " + theItem.link);

            if (theItem.getDiversionInfo()!=null)
            {
                descriptionText.setText("Works: " + theItem.getWorks() + "\n"
                        + "Traffic Management: " + theItem.getManagement() + "\n"
                        + "Diversion: " + theItem.getDiversionInfo() + "\n"
                        + "Link: " + theItem.link);
            }
        }
        else
        {
            descriptionText.setText(theItem.desc + "\n" + theItem.link + "\n" + theItem.getLat() + " " + theItem.getLon());
        }

        if (mapView!= null)
        {
            Log.e("Ye", "hello");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
