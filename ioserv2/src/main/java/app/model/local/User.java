package app.model.local;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import app.model.db.RegisteredUser;
import app.model.form.FormUser;

public class User {
	
	private String id;
	private String password;
	private String avatar;
	private String name;
	private String surname;
	private String phone;
	private String email;	
	@JsonIgnore
	private Map<String, UserItem> items; // no new object; only references to userItems from MainController
	
	public User() {
		this.items = new HashMap<>();
	}
	
	public User(RegisteredUser u) {
		this.id = u.getId();
		this.password = u.getPassword();
		this.avatar = u.getAvatar();
		this.name = u.getName();
		this.surname = u.getSurname();
		this.phone = u.getPhone();
		this.email = u.getEmail();
		this.items = new HashMap<>();
	}
	
	public User(FormUser u) {
		this.id = u.getId();
		this.password = u.getPassword();
		this.avatar = u.getAvatar();
		this.name = u.getName();
		this.surname = u.getSurname();
		this.phone = u.getPhone();
		this.email = u.getEmail();
		this.items = new HashMap<>();
	}
	
	public void update(RegisteredUser u) {
		this.id = u.getId();
		this.password = u.getPassword();
		this.avatar = u.getAvatar();
		this.name = u.getName();
		this.surname = u.getSurname();
		this.phone = u.getPhone();
		this.email = u.getEmail();
	}
	
	public void update(FormUser u) {
		this.id = u.getId();
		this.password = u.getPassword();
		this.avatar = u.getAvatar();
		this.name = u.getName();
		this.surname = u.getSurname();
		this.phone = u.getPhone();
		this.email = u.getEmail();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, UserItem> getItems() {
		return items;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof User 
				&& this.id.equals(((User) obj).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("[id=%s, password=%s, avatar=%s, name=%s, surname=%s, phone=%s, email=%s]",
				id, password, avatar, name, surname, phone, email);
	}
	
}
