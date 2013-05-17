package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {

	private String id;
	private String description;

	public Group(String id, String description) {
		this.id = id;
		this.description = description;
	}

	public Group(JSONObject group) throws JSONException {
		this(group.getString("id"), group.getString("description"));
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString(){
		return id + ": " + description;
	}

	public JSONObject toJsonObject() {
		Map<String, String> elements = new HashMap<String, String>();
		elements.put("id", this.id);
		elements.put("description", this.description);
		return new JSONObject(elements);
	}

	public static final Creator<Group> CREATOR = new Creator<Group>() {
		@Override
		public Group createFromParcel(Parcel source) {
			String id = source.readString();
			String description = source.readString();
			return new Group(id, description);
		}
		@Override
		public Group[] newArray(int size) {
			return new Group[size];
		}
	}; 
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.description);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Group) {
			Group group = (Group) obj;
			return this.id.equals(group.id);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}
