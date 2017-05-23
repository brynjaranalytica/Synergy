package shared;

import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.observer.RemoteObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	User login(String userID, char[] passWord, RemoteObserver<UpdateMessage> client) throws RemoteException;

	String validateId(String userID) throws RemoteException;

	boolean savePass(String userID, char[] pass) throws RemoteException;

	RemoteProjectsInterface getRemoteProjectsForUser(User user) throws RemoteException;
}
