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

	public JSONObject toJsonObject() {
		Map<String, Object> elements = new HashMap<String, Object>();
		elements.put("id", this.id);
		elements.put("position", this.position);
		elements.put("data", this.data);
		return new JSONObject(elements);
	}

}
