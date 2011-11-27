package com.expressway;


import com.expressway.location.EWLocation;
import com.expressway.location.EWLocations;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
        info("v3");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());				
        
        _locations = new EWLocations(this, locationManager);
    }
    
	public class GeoUpdateHandler implements LocationListener {
		
//		@Override
		public void onLocationChanged(Location location) {
//			lat = (int) (location.getLatitude() * 1E6);
//			lng = (int) (location.getLongitude() * 1E6);
//			point = new GeoPoint(lat, lng);
//			mapController.animateTo(point); //
//			mapController.setZoom(16);
//			//mapView.displayZoomControls(true);
//			//mapController.setCenter(point);	
//			//mapController.zoomToSpan(lat, lng);
//			
//			PointerOverlay pointer=new PointerOverlay();
//			List<Overlay> listOfOverlays=mapView.getOverlays();
//			listOfOverlays.clear();
//			listOfOverlays.add(pointer);
//			mapView.invalidate();
//			
//			AWSCredentials cred = new BasicAWSCredentials("AKIAIKU7JE6HDBL2NG7A", "A79SupRoiOMSmQQaQG7bLEorVoQAYBa2qYdl8DX9");
//			
//			AmazonSQSClient sqs = new AmazonSQSClient(cred);
//			
//			sqs.sendMessage(new SendMessageRequest("https://ap-southeast-1.queue.amazonaws.com/471848172502/INPUTQUEUE", lat + "---" + lng));
		}

//		@Override
		public void onProviderDisabled(String provider) {
		}

//		@Override
		public void onProviderEnabled(String provider) {
			//PointerOverlay pointer=new PointerOverlay();
			//List<Overlay> listOfOverlays=mapView.getOverlays();
			//listOfOverlays.clear();
			//listOfOverlays.add(pointer);
			//mapView.invalidate();
		}

//		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
    
    
    
} 
