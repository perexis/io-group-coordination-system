package pl.edu.agh.io.coordinator.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class Layer {
	
	private String name;

	public Layer(String name) {
		super();
		this.name = name;
	}

	public Layer(JSONObject layer) throws JSONException {
		this(layer.getString("name"));
	}
	
	public String getName() {
		return name;
	}
	
	

}
