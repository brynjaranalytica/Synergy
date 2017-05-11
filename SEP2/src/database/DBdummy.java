package database;

import java.rmi.RemoteException;
import java.util.ArrayList;

import server.remote_business_enitities.RProject;
import server.remote_business_enitities.RProjects;
import shared.User;
import shared.UserType;
import utility.Cryptography;

public class DBdummy {
	private ArrayList<User> users;
	private static DBdummy instance = new DBdummy(); //Created in memory beforehand
	
	public static DBdummy getInstance(){
		return instance;
	}

	private DBdummy(){
		users = new ArrayList<>();
		User mogens = new User("mogens@via.dk", "Mogens Bjerregaard", "4593961547", UserType.ADMIN);
		mogens.setPass(Cryptography.encryptPass(new char[] {'m', 'o', 'v', 'e', 'n', '5'}, Cryptography.getKey()));
		users.add(mogens);
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

	public RProjects getRemoteProjects(){
		RProjects remoteProjects = null;
		try {
			remoteProjects = new RProjects();
			RProject remoteProject1 = new RProject("Synergy");
			remoteProject1.addTask("Implement Proxy");
			remoteProject1.addTask("Prototype the database");
			remoteProjects.addProject(remoteProject1);
			remoteProjects.addProject(new RProject("VIA Bus"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return remoteProjects;

	}


	
	
}
