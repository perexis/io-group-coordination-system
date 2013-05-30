package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class UserState {
	
	private Point position;
	private double speed;

	public UserState(Point position, double speed) {
		this.position = position;
		this.speed = speed;
	}

	public UserState(JSONObject userState) throws JSONException {
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

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof UserState) {
			UserState us = (UserState) o;
			if ((us.getPosition().equals(this.getPosition())) && (us.getSpeed() == this.getSpeed())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		Double speed = Double.valueOf(this.getSpeed());
		return this.getPosition().hashCode() + speed.hashCode();
	}
	
}
