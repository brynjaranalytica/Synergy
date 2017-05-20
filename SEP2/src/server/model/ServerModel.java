package server.model;

import server.remote_business_enitities.RProjects;
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

	public RProjects getRemoteProjects(){
		return adapter.getRemoteProjects();
		
		/*RProjects remoteProjects = new RProjects();
		 *remoteProjects.setProjects(ProjectDAO.readAll());  
		 * return remoteProjects;
		 * 
		 * 
		 * */
	}
	
}
