package server.model;

import server.remote_business_enitities.RProjects;
import shared.User;
import shared.business_entities.Project;
import shared.remote_business_interfaces.RemoteProjectInterface;
import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.Cryptography;

import javax.crypto.SecretKey;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerModel {
	//private AdapterInterface adapter;

	private final String COMPANY_NAME = "Neobit";
	private ProjectDAO projectDAO;
	private RemoteProjectsInterface remoteProjects;
	private ArrayList<User> users;


	public ServerModel(){
		//adapter = new Adapter();
		try {
			projectDAO = ProjectDAO.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			this.remoteProjects = this.projectDAO.readAllProjects();
			this.remoteProjects.setName(COMPANY_NAME);
			//this.users = projectDAO.readAllUsers();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public RemoteProjectsInterface getRemoteProjects() {
		return remoteProjects;
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

	public RemoteProjectsInterface getRemoteProjectsForUser(String email){
		//return adapter.getRemoteProjects();
		try {
			return projectDAO.readAllProjectsForUser(email);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

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
	
}
