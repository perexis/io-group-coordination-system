package pl.edu.agh.io.coordinator.resources;

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
	
	public MapItem(JSONObject mapItem) throws JSONException{
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
	
	
	
}
