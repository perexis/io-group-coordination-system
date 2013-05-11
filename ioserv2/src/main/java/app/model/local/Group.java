package app.model.local;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Group {
	private String id;
	private String description;
	@JsonIgnore
	private Map<String, User> users = new HashMap<>();

	public Group() {
	}
	
	public Group(String id, String description) {
		this.id = id;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Map<String, User> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return String.format("[id=%s, description=%s]", id, description);
	}
}
