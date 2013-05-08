package app.model.local;

import app.model.db.RegisteredUserItem;
import app.model.form.FormUserItem;

public class UserItem {
	
	private String id;
	private String description;
	private String image;
	
	public UserItem(RegisteredUserItem i) {
		this.id = i.getId();
		this.description = i.getDescription();
		this.image = i.getImage();
	}
	
	public UserItem(FormUserItem i) {
		this.id = i.getId();
		this.description = i.getDescription();
		this.image = i.getImage();
	}
	
	public void update(RegisteredUserItem i) {
		this.id = i.getId();
		this.description = i.getDescription();
		this.image = i.getImage();
	}
	
	public void update(FormUserItem i) {
		this.id = i.getId();
		this.description = i.getDescription();
		this.image = i.getImage();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof UserItem 
				&& this.id.equals(((UserItem) obj).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("[id=%s, description=%s, image=%s]",
				id, description, image);
	}

}
