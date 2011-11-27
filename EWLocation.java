package com.expressway.location;

public class EWLocation {

	public double lat = 0;
	public double lon = 0;
	public long time = 0;
	
	public void setLocation(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		this.time = System.currentTimeMillis();
	}
}
