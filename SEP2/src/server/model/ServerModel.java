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

public class ServerModel {
	//private AdapterInterface adapter;

	private final String COMPANY_NAME = "Neobit";
	private ProjectDAO projectDAO;
	private RemoteProjectsInterface remoteProjects;
	private ArrayList<User> users;
	private HashMap<String, RemoteObserver<UpdateMessage>> currentConnections;

	private ServerModel(){
		//adapter = new Adapter();
		projectDAO = ProjectDAO.getInstance();
		currentConnections = new HashMap<>();
		try {
			this.remoteProjects = this.projectDAO.readAllProjects();
			this.remoteProjects.setName(COMPANY_NAME);
			this.users = projectDAO.readAllUsers();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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
		RemoteProjectsInterface usersRemoteProjects = null;
		try {
			ArrayList<RemoteProjectInterface> listOfRemoteProjects = remoteProjects.getRemoteProjects();
			 usersRemoteProjects = new RProjects();

			for(String projectName: listOfProjectNamesForUser){
				for(RemoteProjectInterface remoteProject: listOfRemoteProjects){
					if(remoteProject.getName().equals(projectName))
						usersRemoteProjects.addProject(remoteProject);
				}
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return usersRemoteProjects;
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
