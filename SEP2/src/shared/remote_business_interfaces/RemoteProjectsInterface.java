package shared.remote_business_interfaces;

import shared.UpdateMessage;
import shared.business_entities.Project;
import utility.observer.RemoteSubject;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public interface RemoteProjectsInterface extends RemoteSubject<UpdateMessage> {

    ArrayList<RemoteProjectInterface> getRemoteProjects() throws RemoteException;

    void setRemoteProjects(ArrayList<RemoteProjectInterface> remoteProjects) throws RemoteException;

    void addProject(RemoteProjectInterface project) throws RemoteException;

    RemoteProjectInterface getProject(int projectIndex)throws RemoteException;

    RemoteProjectInterface getProject(String projectName)throws RemoteException;

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

    ArrayList<String> getProjectNames() throws RemoteException;
}
