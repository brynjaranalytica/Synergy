package client.model;

import shared.User;

public class ClientModel {
	private User user;
	
	public ClientModel(){
		user = null;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser(){
		return user;
	}

}
