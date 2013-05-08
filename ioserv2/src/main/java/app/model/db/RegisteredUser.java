package app.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import app.model.form.FormUser;

@Entity
@Table(name="Users")
public class RegisteredUser implements Model {
	
	@Id
	@Column
	private String id;
	
	@Column
	private String password;
	
	@Column
	private String avatar;
	
	@Column
	private String name;
	
	@Column
	private String surname;
	
	@Column
	private String phone;
	
	@Column
	private String email;
	
	public RegisteredUser() {
	}
	
	public RegisteredUser(FormUser u) {
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

	public String getPassword() {
		return password;
	}

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
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RegisteredUser 
				&& this.id.equals(((RegisteredUser) obj).id);
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
