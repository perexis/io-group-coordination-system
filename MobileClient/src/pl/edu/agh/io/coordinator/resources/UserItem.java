package pl.edu.agh.io.coordinator.resources;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class UserItem implements Parcelable {

	private String id;
	private String description;
	private String image;

	public UserItem(String id, String description, String image) {
		this.id = id;
		this.description = description;
		this.image = image;
	}

	public UserItem(JSONObject userItem) throws JSONException {
		this(userItem.getString("id"), userItem.getString("description"), userItem.getString("image"));
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public JSONObject toJsonObject() {
		Map<String, String> elements = new HashMap<String, String>();
		elements.put("id", this.id);
		elements.put("description", this.description);
		elements.put("image", this.image);
		return new JSONObject(elements);
	}

	public static final Creator<UserItem> CREATOR = new Creator<UserItem>() {
		@Override
		public UserItem createFromParcel(Parcel source) {
			String id = source.readString();
			String description = source.readString();
			String image = source.readString();
			return new UserItem(id, description, image);
		}
		@Override
		public UserItem[] newArray(int size) {
			return new UserItem[size];
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
		dest.writeString(this.image);
	}

}
