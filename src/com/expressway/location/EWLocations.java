package com.expressway.location;

import java.util.ArrayList;
import java.util.Iterator;

import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.expressway.EWayActivity;

public class EWLocations {
	
	private EWayActivity _eway; // temporary... var not needed for final version.
	
	private LocationManager _locationManager;
	private ArrayList<EWLocation> _locations = new ArrayList<EWLocation>();
	private EWLocation _lastEWLocation = null;
	private boolean _enableMonitoring = false;

	private Handler _timer = new Handler();
	private int 	_triggerCount = 0;

	/* In case of transmission error, locations are stored and re-transmitted later.
	 * But if transmission isn't successful for a long time, say 30 minutes, then
	 * very old location data might not be needed.
	 * Max locations decides this number of locations to store.
	 */
	private int 	_timerPeriod = 5000;
	private int 	_trigger = 6;
	private int 	_maxLocationsToStore = 24;
	
	
	private String 	_userId = "user@domain.com";
	
	private final String _remoteQueueUrl = "https://ap-southeast-1.queue.amazonaws.com/471848172502/android_test";
	private final String _awsKeyId = "AKIAIKU7JE6HDBL2NG7A";
	private final String _awsKey = "A79SupRoiOMSmQQaQG7bLEorVoQAYBa2qYdl8DX9";
	
	public EWLocations(EWayActivity eway, LocationManager locationManager) {
		_eway = eway;
		_locationManager = locationManager;
		startMonitoring();
	}
	
	/**
	 * Monitoring - Periodic detection and transmission of location data.  
	 */
	public void startMonitoring() {
		_timer.postDelayed(refresh, _timerPeriod);
		_enableMonitoring = true;
	}
	
	public void stopMonitoring() {
		_enableMonitoring = false;
	}
	
	
	
	
	/* Read and transmit Location  */
	
	/* Function is automatically called when its time to detect the location */
	private void detectLocation() {
		EWLocation loc = getCurrentLocation();
		_locations.add(loc);
		while (_locations.size() > _maxLocationsToStore) {
			_locations.remove(0);
		}
		_eway.info(getTransmitString());
	}
	
	/* This function gets called when its time to transmit the recorded locations */
	private void transmitLocations() {
		/* In case of transmission error, locations will be stored and sent during next transmission */
		boolean error = false;
		try {
			AWSCredentials cred = new BasicAWSCredentials(_awsKeyId, _awsKey);
			AmazonSQSClient sqs = new AmazonSQSClient(cred);
			sqs.sendMessage(new SendMessageRequest(_remoteQueueUrl, getTransmitString()));
		}
		catch (Exception e) {
			error = true;
		}
		
		if (!error) {
			_eway.info(getTransmitString() + "\n **** TRANSMIT ****");
			_locations.clear();
		} else {
			_eway.info(getTransmitString() + "\n **** TRANSMIT ERROR ****");
		}
	}
	
	private EWLocation getCurrentLocation() {
		// If an error occurs in detecting location, we can simply try again later.
		// But during development, it may be useful to see what kind of errors are happening.
		// So, latitude = 0 indicates error and longitude value indicates the reason for error.

		boolean error = false;
		int errorVal = 0;
		Location gpsLocation = null;
		
		try {
			gpsLocation = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (gpsLocation == null) {
				errorVal = 0;
				error = true;
			}
		}
		catch (SecurityException e) {
			errorVal = 1;
			error = true;
		}
		catch (IllegalArgumentException e) {
			errorVal = 2;
			error = true;
		}
		catch (Exception e) { // Unknown error
			errorVal = 3;
			error = true;
		}
		
		
		EWLocation curLocation = new EWLocation();
		
		if (!error) {
			if (_lastEWLocation == null) {
				curLocation.setLocation(gpsLocation);
			} else if (_lastEWLocation.isValid()) {
				curLocation.setLocation(gpsLocation, _lastEWLocation);
			} else {
				curLocation.setLocation(gpsLocation);
			}
			_lastEWLocation = curLocation;
		}
		else {
			curLocation.setLocation(errorVal); 
		}
		
		return curLocation;
	}
	
	private String getTransmitString() {
		String str = "";
		str += _userId + "\n";
		Iterator<EWLocation> i = _locations.iterator();
		while(i.hasNext()) {
			str += i.next().getTransmitString() + "\n";
		}
		return str;
	}
	
	
	/* Scheduling to detect and transmit data. */
	
	private Runnable refresh = new Runnable() {
		public void run() {
			detectLocation();
			if (isScheduledForTransmission()) {
				transmitLocations();
			}
			if (_enableMonitoring) {
				_timer.postDelayed(refresh, _timerPeriod);
			}
		}
	};
	
	private boolean isScheduledForTransmission() {
		if (_triggerCount < (_trigger-1)) {
			_triggerCount++;
			return false;
		} else {
			_triggerCount = 0;
			return true;
		}
	}
	
}
