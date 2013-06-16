/*
 * Copyright 2013
 * Piotr Bryk, Wojciech Grajewski, Rafał Szalecki, Piotr Szmigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Point) {
			Point p = (Point) o;
			if ((p.getLatitude() == this.getLatitude()) && (p.getLongitude() == this.getLongitude())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		Double latitude = Double.valueOf(this.getLatitude());
		Double longitude = Double.valueOf(this.getLongitude());
		return latitude.hashCode() + longitude.hashCode();
	}

}

