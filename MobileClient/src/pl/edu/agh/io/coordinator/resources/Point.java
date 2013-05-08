package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

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
	
	public JSONObject toJsonObject() {
		Map<String, Object> elements = new HashMap<String, Object>();
		elements.put("latitude", this.latitude);
		elements.put("longitude", this.longitude);
		return new JSONObject(elements);
	}
	
	
}
