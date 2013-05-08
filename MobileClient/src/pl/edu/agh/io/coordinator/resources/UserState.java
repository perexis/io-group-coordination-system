package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class UserState {
	private Point position;
	private double speed;
	
	public UserState(Point position, double speed) {
		this.position=position;
		this.speed=speed;
	}
	
	public UserState(JSONObject userState) throws JSONException{
		this(new Point(userState.getJSONObject("position")), userState.getDouble("speed"));
	}

	public Point getPosition() {
		return position;
	}

	public double getSpeed() {
		return speed;
	}
	
	public JSONObject toJsonObject() {
		Map<String, Object> elements = new HashMap<String, Object>();
		elements.put("position", this.position.toJsonObject());
		elements.put("speed", this.speed);
		return new JSONObject(elements);
	}
	
}
