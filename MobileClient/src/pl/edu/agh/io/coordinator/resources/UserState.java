/*
 * Copyright 2013
 * Piotr Bryk, Wojciech Grajewski, Rafał Szalecki, Piotr Szmigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

