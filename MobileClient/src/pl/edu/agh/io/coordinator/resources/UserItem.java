package pl.edu.agh.io.coordinator.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class UserItem {

	private String id;
	private String description;
	private String image;
	public UserItem(String id, String description, String image) {
		this.id = id;
		this.description = description;
		this.image = image;
	}
	
	public UserItem(JSONObject userItem) throws JSONException{
		this(userItem.getString("id"), userItem.getString("description"), userItem.getString("image"));
	}
	
	public String getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public String getImage() {
		return image;
	}
	
	
	
	
	
}
