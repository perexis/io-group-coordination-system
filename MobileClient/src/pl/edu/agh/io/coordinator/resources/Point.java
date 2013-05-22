package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class Point {

	//private double latitude;
	//private double longitude;
	
	private LatLng coords;

	public Point(double latitude, double longitude) {
		coords = new LatLng(latitude, longitude);
	}

	public Point(JSONObject point) throws JSONException {
		this(point.getDouble("latitude"), point.getDouble("longitude"));
	}

	public double getLatitude() {
		return coords.latitude;
	}

	public double getLongitude() {
		return coords.longitude;
	}
	
	public LatLng getLatLng(){
		return coords;
	}

	public JSONObject toJsonObject() {
		Map<String, Double> elements = new HashMap<String, Double>();
		elements.put("latitude", this.coords.latitude);
		elements.put("longitude", this.coords.longitude);
		return new JSONObject(elements);
	}

}
