package server.model;

import server.remote_business_enitities.RProjects;
import shared.UpdateMessage;
import shared.User;
import shared.remote_business_interfaces.RemoteProjectInterface;
import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.Cryptography;
import utility.observer.RemoteObserver;

import javax.crypto.SecretKey;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used for "caching" the data extracted from the database. It also represents an additional layer
 * between server controller and database. This class represents a part of server side's MVC pattern - Model. This class
 * also stores instances of RemoteObserver class, which represent current client connections. This class is a Singleton.
 */
public class ServerModel {
	private final String COMPANY_NAME = "Neobit";
	private ProjectDAO projectDAO;
	private RemoteProjectsInterface remoteProjects;
	private ArrayList<User> users;
	private HashMap<String, RemoteObserver<UpdateMessage>> currentConnections;
	private HashMap<String, RemoteProjectsInterface> projectSets;

	private ServerModel(){
		projectDAO = ProjectDAO.getInstance();
		currentConnections = new HashMap<>();
		projectSets = new HashMap<>();
		try {
			this.remoteProjects = this.projectDAO.readAllProjects();
			this.remoteProjects.setName(COMPANY_NAME);
			this.users = projectDAO.readAllUsers();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void addProjectSet(String email, RemoteProjectsInterface projectSet){
		this.projectSets.put(email, projectSet);
	}

	public RemoteProjectsInterface getProjectSet(String email){
		return this.projectSets.get(email);
	}

	public void addConnection(String email, RemoteObserver<UpdateMessage> client){
		this.currentConnections.put(email, client);
	}

	public RemoteObserver<UpdateMessage> getConnection(String email){
		return this.currentConnections.get(email);
	}

	public void removeConnection(String email){
		this.currentConnections.remove(email);
	}

	public RemoteProjectsInterface getRemoteProjects() {
		return remoteProjects;
	}

	public RemoteProjectsInterface getRemoteProjectsForUser(String email){
		//return adapter.getRemoteProjects();

		ArrayList<String> listOfProjectNamesForUser = projectDAO.readProjectNamesForUser(email);
		ArrayList<RemoteProjectInterface> usersListOfRemoteProjects = null;
		try {
			ArrayList<RemoteProjectInterface> listOfRemoteProjects = this.remoteProjects.getRemoteProjects();
			 usersListOfRemoteProjects = new ArrayList<>();

			for(String projectName: listOfProjectNamesForUser){
				for(RemoteProjectInterface remoteProject: listOfRemoteProjects){
					if(remoteProject.getName().equals(projectName))
						usersListOfRemoteProjects.add(remoteProject);
				}
			}

			RemoteProjectsInterface usersRemoteProjects = new RProjects();
			usersRemoteProjects.setProjects(usersListOfRemoteProjects);
			usersRemoteProjects.setMembers(this.remoteProjects.getMembers());
			return usersRemoteProjects;

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}




	public User getUser(String id){
		User user = retrieveUser(id);
		char[] passEncrypted = user.getPass();
		char[] passDecrypted = Cryptography.decryptPass(passEncrypted, Cryptography.getKey());
		user.setPass(passDecrypted); //should be changed - stores decrypted pw back?
		return user;
	}

	public User retrieveUser(String id){
		if (id == null) return null;
		for (User u: users) {
			String userID = u.getiD();
			if (id.equalsIgnoreCase(userID)) {
				User result = u.copyOf();
				return result;
			}
		}
		return null;
	}

	/*public String validateID(String userID){
		return adapter.validateID(userID);
	}*/

	public boolean savePass(String userID, char[] pass) {
		SecretKey key = Cryptography.getKey();
		char[] passEncrypted = (Cryptography.encryptPass(pass, key));
		try {
			archiveNewPassword(userID,passEncrypted);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*public User login(String userID, char[] passWord) {
		return adapter.login(userID, passWord);
	}*/

	public char[] getPass(String userID) {
		return Cryptography.decryptPass(retrievePassword(userID), Cryptography.getKey());
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

	private static class Wrapper { //Instance placed in inner class
		static ServerModel instance = new ServerModel();
	}

	public static ServerModel getInstance() {
		return Wrapper.instance; //Instantiates the instance when called
	}
	
}
