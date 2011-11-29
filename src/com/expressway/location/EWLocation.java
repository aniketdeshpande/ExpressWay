package com.expressway.location;

import android.location.Location;

public class EWLocation {

	public double lat = 0;
	public double lon = 0;
	public long time = 0;
	public double speed = -1;
	public Location gpsLocation;
	
	public void setLocation(double errorValue) {
		this.lat = 0;
		this.lon = errorValue;
		this.time = System.currentTimeMillis();
	}

	public void setLocation(Location loc) {
		this.lat = loc.getLatitude();
		this.lon = loc.getLongitude();
		this.time = System.currentTimeMillis();
		this.gpsLocation = loc;
	}
	
	public void setLocation(Location loc, EWLocation previousLoc) {
		setLocation(loc);
		this.speed = getSpeed(previousLoc);
	}
	
	public boolean isValid() {
		return (this.gpsLocation != null);
	}
	
	public String getTransmitString() {
		String val = lat + "---" + lon + "---" + time + "---";
		if (this.speed < -0.1) {
			val += "*";
		} else {
			val += this.speed;
		}
		return val; 
	}
	
	private double getSpeed(EWLocation previous) {
		if (this.time == 0) {
			return -1;
		}
		// If current or previous location is invalid, return negative.
		float dMeters = this.gpsLocation.distanceTo(previous.gpsLocation);
		long dMilliSecs = (this.time - previous.time);
		double kmph = 3600*dMeters/dMilliSecs; // (dMeters/1000)/(dMilliSecs/3600000)
		return kmph;
	}
	
	
}
