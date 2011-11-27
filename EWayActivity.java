package com.expressway;

import com.expressway.location.EWLocation;
import com.expressway.location.EWLocations;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EWayActivity extends Activity {
	
	private EWLocations _locations;
	
	public void locationChanged(EWLocation location) {
        info(String.valueOf(location.lat) + "---" +
        		String.valueOf(location.lon) + "---" +
        		String.valueOf(location.time));
	}
	
	private void info(String msg) {
        TextView myText = (TextView) findViewById(R.id.txtInfo);
        myText.setText(msg);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        info("Using INPUTQUEUE");
        _locations = new EWLocations(this, (LocationManager) getSystemService(Context.LOCATION_SERVICE));
    }
    
    
}