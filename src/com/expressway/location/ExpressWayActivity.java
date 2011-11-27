package com.expressway.location;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amazonaws.auth.*;
import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ExpressWayActivity extends MapActivity {

	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	GeoPoint point;
	int lat;
	int lng;
	
	class PointerOverlay extends com.google.android.maps.Overlay
	{
		@Override
		public boolean draw (Canvas canvas, MapView mapView, boolean shadow, long when)
		{
			super.draw(canvas, mapView, shadow);
			
			Point screenPts=new Point();
			mapView.getProjection().toPixels(point, screenPts);
			
			Bitmap gif=BitmapFactory.decodeResource(getResources(),R.drawable.pin);
			
			canvas.drawBitmap(gif,screenPts.x,screenPts.y,null);
			
			Toast.makeText(ExpressWayActivity.this, lat + "---" + lng, 300).show();
			
			return true;
		}
	}	
	
		@Override
		public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.main); // bind the layout to the activity
		
		AWSCredentials cred = new BasicAWSCredentials("AKIAIKU7JE6HDBL2NG7A", "A79SupRoiOMSmQQaQG7bLEorVoQAYBa2qYdl8DX9");
		
		AmazonSQSClient sqs = new AmazonSQSClient(cred);
		
		sqs.sendMessage(new SendMessageRequest("https://ap-southeast-1.queue.amazonaws.com/471848172502/INPUTQUEUE", "Message 123"));
		
		// create a map view
		@SuppressWarnings("unused")
		RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setStreetView(true);
		//LinearLayout zoomLayout=(LinearLayout)findViewById(R.id.zoom);
		//View zoomView = mapView.getZoomControls(); 
		
		//zoomLayout.addView(zoomView,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		//mapView.displayZoomControls(true);
		
		mapController = mapView.getController();
		mapController.setZoom(14); 
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());				
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public class GeoUpdateHandler implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			lat = (int) (location.getLatitude() * 1E6);
			lng = (int) (location.getLongitude() * 1E6);
			point = new GeoPoint(lat, lng);
			mapController.animateTo(point); //
			mapController.setZoom(16);
			//mapView.displayZoomControls(true);
			//mapController.setCenter(point);	
			//mapController.zoomToSpan(lat, lng);
			
			PointerOverlay pointer=new PointerOverlay();
			List<Overlay> listOfOverlays=mapView.getOverlays();
			listOfOverlays.clear();
			listOfOverlays.add(pointer);
			mapView.invalidate();
			
			AWSCredentials cred = new BasicAWSCredentials("AKIAIKU7JE6HDBL2NG7A", "A79SupRoiOMSmQQaQG7bLEorVoQAYBa2qYdl8DX9");
			
			AmazonSQSClient sqs = new AmazonSQSClient(cred);
			
			sqs.sendMessage(new SendMessageRequest("https://ap-southeast-1.queue.amazonaws.com/471848172502/INPUTQUEUE", lat + "---" + lng));
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			//PointerOverlay pointer=new PointerOverlay();
			//List<Overlay> listOfOverlays=mapView.getOverlays();
			//listOfOverlays.clear();
			//listOfOverlays.add(pointer);
			//mapView.invalidate();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
