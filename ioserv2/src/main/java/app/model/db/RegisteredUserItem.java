package app.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import app.model.form.FormUserItem;

@Entity
@Table(name="UserItems")
public class RegisteredUserItem implements Model {
	
	@Id
	@Column
	private String id;
	
	@Column
	private String description;
	
	@Column
	private String image;
	
	public RegisteredUserItem() {
	}
	
	public RegisteredUserItem(FormUserItem form) {
		this.id = form.getId();
		this.description = form.getDescription();
		this.image = form.getImage();
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
		return obj instanceof RegisteredUserItem 
				&& this.id.equals(((RegisteredUserItem) obj).id);
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
