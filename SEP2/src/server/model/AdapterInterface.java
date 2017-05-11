package server.model;

import server.remote_business_enitities.RProjects;
import shared.User;

public interface AdapterInterface {
	User getUser(String userID);

	String validateID(String userID);

	boolean savePass(String userID, char[] pass);

	char[] getPass(String userID);

	RProjects getRemoteProjects();

	User login(String userID, char[] passWord);
}
