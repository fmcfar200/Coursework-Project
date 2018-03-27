/*
    Fraser McFarlane S1434566
*/


package mpdproject.gcu.frasermcfarlane.org.courseworkassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button incidentsButton;
    private Button plannedRWButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incidentsButton = (Button) findViewById(R.id.incidentsButton);
        incidentsButton.setOnClickListener(this);

        plannedRWButton = (Button) findViewById(R.id.plannedRWButton);
        plannedRWButton.setOnClickListener(this);

    }

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

}
