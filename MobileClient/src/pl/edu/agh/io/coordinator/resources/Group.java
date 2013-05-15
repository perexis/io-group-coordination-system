package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

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
	
	@Override
	public String toString(){
		return id + ": " + description;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Group)
			return this.id.equals(((Group)o).getId());
		else
			return false;
	}

	public JSONObject toJsonObject() {
		Map<String, String> elements = new HashMap<String, String>();
		elements.put("id", this.id);
		elements.put("description", this.description);
		return new JSONObject(elements);
	}

}
