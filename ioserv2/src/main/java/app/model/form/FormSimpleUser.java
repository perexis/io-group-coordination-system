package app.model.form;

/* Used only as admin-logging form backend */
public class FormSimpleUser {
	
	private String login;
	private String password;
	
	public FormSimpleUser() {
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
