package shared.remote_business_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by lenovo on 5/11/2017.
 */
public interface RemoteMemoInterface extends Remote {
    String getDescription() throws RemoteException;

    Date getDate() throws RemoteException;

    void setDescription(String description) throws RemoteException;

    void setDate(Date date) throws RemoteException;
}
