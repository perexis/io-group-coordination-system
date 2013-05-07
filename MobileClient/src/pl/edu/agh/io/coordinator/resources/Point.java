package pl.edu.agh.io.coordinator.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class Point {
	private double latitude;
	private double longitude;
	
	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Point(JSONObject point) throws JSONException{
		this(point.getDouble("latitude"), point.getDouble("longitude"));
	}
	
	public double getLatitude() {

		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	
	
	
}
