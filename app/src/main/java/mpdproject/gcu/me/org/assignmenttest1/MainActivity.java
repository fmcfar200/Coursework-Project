//
//
// Starter code for the Mobile Platform Development Assignment
// Seesion 2017/2018
//
//

package mpdproject.gcu.me.org.assignmenttest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button incidentsButton;
    private Button plannedRWButton;

    private enum fetchType
    {
        ROADWORKS,
        INCIDENTS,
        PLANNED_RW
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incidentsButton = (Button)findViewById(R.id.incidentsButton);
        incidentsButton.setOnClickListener(this);

        plannedRWButton = (Button)findViewById(R.id.plannedRWButton);
        plannedRWButton.setOnClickListener(this);

    } // End of onCreate

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
