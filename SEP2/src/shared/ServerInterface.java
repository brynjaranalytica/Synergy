package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	public User login(String userID, char[] passWord) throws RemoteException;
	public String validateId(String userID) throws RemoteException;
	public boolean savePass(String userID, char[] pass) throws RemoteException;
}
