package pl.edu.agh.io.coordinator.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class Group {
	private String id;
	private String description;

	public Group(String id, String description) {
		this.id = id;
		this.description = description;
	}

	public Group(JSONObject group) throws JSONException {
		this(group.getString("id"), group.getString("description"));
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

}
