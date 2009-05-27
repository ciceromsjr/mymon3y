package com.google.code.mymon3y.jsf;

public class LoginManager {
	
	private String user = "";
	private String password = "";

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String loginAction() {
		if ("Batata".equalsIgnoreCase(user))
			return "loginPass";
		else
			return "loginFail";
	}
}
