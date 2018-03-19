package mpdproject.gcu.me.org.assignmenttest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ItemDetail extends AppCompatActivity {

    private TextView titleText;
    private TextView descriptionText;

    RoadWorksItem theItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        titleText = (TextView)findViewById(R.id.titleID);
        descriptionText = (TextView)findViewById(R.id.descriptionID);

        theItem = (RoadWorksItem) getIntent().getSerializableExtra("RoadWorksItem");

        titleText.setText(theItem.title);

        if (theItem.getWorks() != null)
        {
            descriptionText.setText("Works: " + theItem.getWorks() +"\n"+ "Traffic Management: " + theItem.getManagement());

            if (theItem.getDiversionInfo()!=null)
            {
                descriptionText.setText("Works: " + theItem.getWorks() + "\n" + "Traffic Management: " + theItem.getManagement() + "\n" + "Diversion: " + theItem.getDiversionInfo());
            }
        }
        else
        {
            descriptionText.setText(theItem.desc);
        }
    }
}
