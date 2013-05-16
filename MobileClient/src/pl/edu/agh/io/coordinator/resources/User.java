package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class User implements Parcelable {

	private String id;
	private String avatar;
	private String name;
	private String surname;
	private String phone;
	private String email;

	public User(String id, String avatar, String name, String surname, String phone, String email) {
		this.id = id;
		this.avatar = avatar;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.email = email;
	}

	public User(JSONObject user) throws JSONException {
		this(user.getString("id"), user.getString("avatar"), user.getString("name"), user.getString("surname"), user
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

	public JSONObject toJsonObject() {
		Map<String, String> elements = new HashMap<String, String>();
		elements.put("id", this.id);
		elements.put("avatar", this.avatar);
		elements.put("name", this.name);
		elements.put("surname", this.surname);
		elements.put("phone", this.phone);
		elements.put("email", this.email);
		return new JSONObject(elements);
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			String id = source.readString();
			String avatar = source.readString();
			String name = source.readString();
			String surname = source.readString();
			String phone = source.readString();
			String email = source.readString();
			return new User(id, avatar, name, surname, phone, email);
		}
		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	}; 
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.avatar);
		dest.writeString(this.name);
		dest.writeString(this.surname);
		dest.writeString(this.phone);
		dest.writeString(this.email);
	}
	
}
