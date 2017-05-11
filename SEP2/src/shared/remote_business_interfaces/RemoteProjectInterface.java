package shared.remote_business_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public interface RemoteProjectInterface extends Remote {
    String getName() throws RemoteException;

    ArrayList<RemoteMemberInterface> getMembers() throws RemoteException;

    void setName(String name) throws RemoteException;

    void setMembers(ArrayList<RemoteMemberInterface> members) throws RemoteException;

    RemoteTaskListInterface getTaskList()throws RemoteException;

    void setTaskList(RemoteTaskListInterface taskList) throws RemoteException;

    void addTask(String task) throws RemoteException;

    void addMember(RemoteMemberInterface member) throws RemoteException;

    RemoteMemberInterface getMember(int index) throws RemoteException;
}
