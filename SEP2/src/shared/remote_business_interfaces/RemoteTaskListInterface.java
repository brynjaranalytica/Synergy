package shared.remote_business_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public interface RemoteTaskListInterface extends Remote {
    String getTask(int index) throws RemoteException;

    void addTask(String task) throws RemoteException;

    ArrayList<String> getTasks() throws RemoteException;
}
