package server.model;

import shared.User;

public class ServerModel {
	private AdapterInterface adapter;
	public ServerModel(){
		adapter = new Adapter();
	}
	
	public User getUser(String id){
		return adapter.getUser(id);
	}
	public String validateID(String userID){
		return adapter.validateID(userID);
	}

	public boolean savePass(String userID, char[] pass) {
		return adapter.savePass(userID, pass);
	}

	public User login(String userID, char[] passWord) {
		return adapter.login(userID, passWord);
	}

	
}
