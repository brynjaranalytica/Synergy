package shared.remote_business_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by lenovo on 4/12/2017.
 */
public interface RemoteMemberInterface extends Remote{
    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

    String getEmail() throws RemoteException;

    void setEmail(String email) throws RemoteException;
}
