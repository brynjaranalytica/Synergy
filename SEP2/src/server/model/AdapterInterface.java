package server.model;

import shared.User;

public interface AdapterInterface {
	User getUser(String userID);
	String validateID(String userID);
	boolean savePass(String userID, char[] pass);
	char[] getPass(String userID);
	User login(String userID, char[] passWord);
}
