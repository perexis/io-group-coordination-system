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

public class MapItem {

	private long id;
	private Point position;
	private String data;

	public MapItem(long id, Point position, String data) {
		this.id = id;
		this.position = position;
		this.data = data;
	}

	public MapItem(JSONObject mapItem) throws JSONException {
		this(mapItem.getLong("id"), new Point(mapItem.getJSONObject("position")), mapItem.getString("data"));
	}

	public long getId() {
		return id;
	}

	public Point getPosition() {
		return position;
	}

	public String getData() {
		return data;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof MapItem)
			return ((MapItem) o).getId() == this.getId();
		else
			return false;
	}

	@Override
	public int hashCode(){
		return Long.valueOf(id).hashCode();
	}
	
	public JSONObject toJsonObject() {
		Map<String, Object> elements = new HashMap<String, Object>();
		elements.put("id", this.id);
		elements.put("position", this.position);
		elements.put("data", this.data);
		return new JSONObject(elements);
	}

}

