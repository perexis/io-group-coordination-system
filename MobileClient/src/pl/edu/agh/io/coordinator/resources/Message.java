package pl.edu.agh.io.coordinator.resources;

import org.json.JSONObject;

public class Message {
	private long sentTime; //maybe unix time?
	private String userName;
	private String text;
	
	public Message(JSONObject message){
		//TODO: implement
	}

	public long getSentTime() {
		return sentTime;
	}

	public String getUserName() {
		return userName;
	}

	public String getText() {
		return text;
	}
	
}
