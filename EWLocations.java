package com.expressway.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.expressway.EWayActivity;

public class EWLocations {
	private EWayActivity _eway;
	private EWLocation _location;
	private Handler _refreshScheduler;
	private long _refreshPeriod = 5000;
	private LocationManager _locationManager;
	
	public EWLocations(EWayActivity eway, LocationManager locationManager) {
		_eway = eway;
		_locationManager = locationManager;
		_location = new EWLocation();
		_location.setLocation(0, 0);
		_refreshScheduler = new Handler();
		_refreshScheduler.postDelayed(refresh, _refreshPeriod);
	}
	
	public Runnable refresh = new Runnable() {
		public void run() {
			Location loc = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (loc != null) {
				_location.setLocation(loc.getLatitude(), loc.getLongitude());
			} 
			else {
				_location.setLocation(0, 0);
			}
			_eway.locationChanged(_location);
			submit(_location);
			_refreshScheduler.postDelayed(refresh, _refreshPeriod);
		}
	};
	
	public void submit(EWLocation location) {
		AWSCredentials cred = new BasicAWSCredentials("AKIAIKU7JE6HDBL2NG7A", "A79SupRoiOMSmQQaQG7bLEorVoQAYBa2qYdl8DX9");
		AmazonSQSClient sqs = new AmazonSQSClient(cred);
		sqs.sendMessage(new SendMessageRequest("https://ap-southeast-1.queue.amazonaws.com/471848172502/INPUTQUEUE",
			location.lat + "---" + location.lon + "---" + location.time));
	}
	
}
