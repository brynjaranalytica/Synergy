package server.model;

import server.remote_business_enitities.RProjects;
import shared.User;
import shared.business_entities.Project;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class ServerModel {
	private AdapterInterface adapter;
	private ProjectDAO projectDAO;

	public ServerModel(){
		adapter = new Adapter();
		try {
			projectDAO = ProjectDAO.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public RProjects getRemoteProjects(){
		//return adapter.getRemoteProjects();

		RProjects remoteProjects = null;
		try {
			remoteProjects = new RProjects();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			remoteProjects.setProjects(projectDAO.readAllProjects());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return remoteProjects;



	}
	
}
