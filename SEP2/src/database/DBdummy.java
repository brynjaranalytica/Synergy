package database;

import java.rmi.RemoteException;
import java.util.ArrayList;

import server.remote_business_enitities.RMemo;
import server.remote_business_enitities.RProject;
import server.remote_business_enitities.RProjects;
import shared.User;
import shared.UserType;
import shared.remote_business_interfaces.RemoteCalendarInterface;
import shared.remote_business_interfaces.RemoteProjectInterface;
import utility.Cryptography;
import utility.Utilities;

public class DBdummy {
	private ArrayList<User> users;
	private static DBdummy instance = new DBdummy(); //Created in memory beforehand

	private RProjects remoteProjects;


	public static DBdummy getInstance(){
		return instance;
	}

	private DBdummy(){
		users = new ArrayList<>();
		User mogens = new User("mogens@via.dk", "Mogens Bjerregaard", "4593961547", UserType.ADMIN);
		mogens.setPass(Cryptography.encryptPass(new char[] {'m', 'o', 'v', 'e', 'n', '5'}, Cryptography.getKey()));
		users.add(mogens);
		User nicolai = new User("253739@VIA.DK", "Nick Onov", "4581929966", UserType.USER);
		nicolai.setPass(Cryptography.encryptPass(new char[]{'1','1','1','1','1','1'}, Cryptography.getKey()));
		users.add(nicolai);
		try {
			remoteProjects = new RProjects();


			RProject remoteProject1 = new RProject("Synergy");
			RemoteCalendarInterface calendar = remoteProject1.getCalendar();
			calendar.addMemo(new RMemo(Utilities.parseDate("2017-05-12"),"Skype meeting"));
			calendar.addMemo(new RMemo(Utilities.parseDate("2017-05-15"),"Sprint 4 start"));
			remoteProject1.addTask("Implement Proxy");
			remoteProject1.addTask("Prototype the database");
			remoteProjects.addProject(remoteProject1);

			remoteProjects.addProject(new RProject("VIA Bus"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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
		return remoteProjects;
	}

	public void updateProject(RemoteProjectInterface updatedProject){
		try {
			ArrayList<RemoteProjectInterface> projectList = remoteProjects.getRemoteProjects();
			for(int i = 0; i < projectList.size(); i++) {
                if(projectList.get(i).getName().equals(updatedProject.getName())){
                    projectList.set(i,updatedProject);
                }
            }
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}


	
	
}
