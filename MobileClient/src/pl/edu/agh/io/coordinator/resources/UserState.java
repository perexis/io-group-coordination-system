package pl.edu.agh.io.coordinator.resources;

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
	
	
	
}
