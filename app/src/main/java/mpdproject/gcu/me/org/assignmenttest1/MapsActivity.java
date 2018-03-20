package mpdproject.gcu.me.org.assignmenttest1;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView titleText;
    private TextView descriptionText;



    private GoogleMap mMap;
    RoadWorksItem theItem;
    LatLng theLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        theItem = (RoadWorksItem) getIntent().getSerializableExtra("RoadWorksItem");
        titleText = (TextView)findViewById(R.id.titleID);
        descriptionText = (TextView)findViewById(R.id.descriptionID);

        if (theItem!=null)
        {
            theLatLng = new LatLng(theItem.getLat(),theItem.getLon());
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


        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng thePosition = theLatLng;
        mMap.addMarker(new MarkerOptions().position(thePosition).title(theItem.title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(thePosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thePosition,12.5f));
    }
}
