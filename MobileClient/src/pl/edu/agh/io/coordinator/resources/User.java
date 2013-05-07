package pl.edu.agh.io.coordinator.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private String id;
	private String avatar;
	private String name;
	private String surname;
	private String phone;
	private String email;

	public User(String id, String avatar, String name, String surname,
			String phone, String email) {
		this.id = id;
		this.avatar = avatar;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.email = email;
	}

	public User(JSONObject user) throws JSONException {
		this(user.getString("id"), user.getString("avatar"), user
				.getString("name"), user.getString("surname"), user
				.getString("phone"), user.getString("email"));
	}

	public String getId() {
		return id;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

}
