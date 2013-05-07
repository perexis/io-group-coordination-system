package pl.edu.agh.io.coordinator.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
	private long sentTime; // maybe unix time (UTC)?
	private String userID; // "id" in JSON
	private String text;

	public Message(long sentTime, String userID, String text) {
		super();
		this.sentTime = sentTime;
		this.userID = userID;
		this.text = text;
	}

	public Message(JSONObject message) throws JSONException {
		this(message.getLong("sentTime"), message.getString("id"), message
				.getString("text"));
	}

	public long getSentTime() {
		return sentTime;
	}

	public String getUserID() {
		return userID;
	}

	public String getText() {
		return text;
	}

}
