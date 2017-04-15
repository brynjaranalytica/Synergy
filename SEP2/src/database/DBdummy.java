package database;

import java.util.ArrayList;

import shared.User;
import shared.UserType;

public class DBdummy {
	private ArrayList<User> users;
	
	public DBdummy(){
		users = new ArrayList<>();
		users.add(new User("mogens.bjerregaard@mac.com", "Mogens Bjerregaard", "4593961547", UserType.ADMIN));
		users.add(new User("253739@VIA.DK", "Nick Onov", "4581929966", UserType.USER));
	}
	public User retrieveUser(String id){
		if (id == null) return null;
		for (User u: users) {
			if (id.equalsIgnoreCase(u.getiD())) return u;
		}
		return null;
	}
	public String checkID(String userID) {
		for (User u: users){
			if (u.getiD().equalsIgnoreCase(userID)) return u.getPhone();
		}
		return null;
	}
	public void archiveNewPassword(String userID, char[] pass) throws Exception {
		for (User u: users) {
			if (u.getiD().equalsIgnoreCase(userID)){
				u.setPass(pass);
				return;
			} 
		}
		throw new Exception();
	}
	public char[] retrievePassword(String userID) {
		for (User u: users) {
			if (userID.equalsIgnoreCase(u.getiD())) return u.getPass();
		}
		return null;
	}
	
	
}
