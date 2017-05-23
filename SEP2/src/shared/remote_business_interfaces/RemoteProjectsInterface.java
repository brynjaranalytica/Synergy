package shared.remote_business_interfaces;

import shared.business_entities.ProjectInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public interface RemoteProjectsInterface extends Remote {

    ArrayList<RemoteProjectInterface> getRemoteProjects() throws RemoteException;

    void setRemoteProjects(ArrayList<RemoteProjectInterface> remoteProjects) throws RemoteException;

    void addProject(RemoteProjectInterface project, String emailOfCreator) throws RemoteException;

    void addProject(ProjectInterface project, String emailOfCreator) throws RemoteException;

    RemoteProjectInterface getProject(int projectIndex)throws RemoteException;

    RemoteProjectInterface getProject(String projectName)throws RemoteException;

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

    ArrayList<String> getProjectNames() throws RemoteException;

    void removeProject(String projectName) throws RemoteException;

    ArrayList<RemoteMemberInterface> getMembers() throws RemoteException;

    void setMembers(ArrayList<RemoteMemberInterface> members) throws RemoteException;

    RemoteMemberInterface getMember(int index) throws RemoteException;

    RemoteMemberInterface getMember(String email) throws RemoteException;

    void setProjects(ArrayList<RemoteProjectInterface> remoteProjects) throws RemoteException;

}
