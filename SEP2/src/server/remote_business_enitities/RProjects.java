package server.remote_business_enitities;

import server.model.ProjectDAO;
import server.model.ServerModel;
import shared.MessageHeaders;
import shared.UpdateMessage;
import shared.business_entities.BusinessEntity;
import shared.business_entities.Member;
import shared.business_entities.Project;
import shared.business_entities.ProjectInterface;
import shared.remote_business_interfaces.RemoteMemberInterface;
import shared.remote_business_interfaces.RemoteMemoInterface;
import shared.remote_business_interfaces.RemoteProjectInterface;
import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.observer.RemoteObserver;
import utility.observer.RemoteSubjectDelegate;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public class RProjects implements RemoteProjectsInterface {
    //private static RemoteProjectsInterface mirror;
    //private static RemoteSubjectDelegate<UpdateMessage> remoteSubjectDelegate;

    static  ArrayList<RemoteMemberInterface> remoteMembers;
    private ArrayList<RemoteProjectInterface> remoteProjects;
    private String name;

    public RProjects(shared.business_entities.Projects projects) throws RemoteException  {
        this.remoteProjects = new ArrayList<>();
        this.name = projects.getName();
        this.remoteProjects = new ArrayList<>();
        ArrayList<Member> members = projects.getMembers();
        for(Member member: members){
            this.remoteMembers.add(new RMember(member));
        }
        ArrayList<ProjectInterface> projectList = projects.getProjectList();
        for(ProjectInterface project: projectList){
            this.remoteProjects.add(new RProject(project));
        }
        UnicastRemoteObject.exportObject(this,0);
        //mirror = this;

    }

    public ArrayList<RemoteMemberInterface> getMembers() throws RemoteException{
        return remoteMembers;
    }

    @Override
    public void setMembers(ArrayList<RemoteMemberInterface> members) throws RemoteException {
        this.remoteMembers = members;
    }

    @Override
    public RemoteMemberInterface getMember(int index) throws RemoteException {
        return this.remoteMembers.get(index);
    }

    @Override
    public RemoteMemberInterface getMember(String email) throws RemoteException {
        for(RemoteMemberInterface member: remoteMembers){
            if(member.getEmail().equals(email)) {
                return member;
            }
        }

        return null;
    }

    @Override
    public void setProjects(ArrayList<RemoteProjectInterface> remoteProjects) throws RemoteException {
        this.remoteProjects = remoteProjects;
    }

    public RProjects() throws RemoteException{
        //remoteSubjectDelegate = new RemoteSubjectDelegate<>(this);
        this.remoteProjects = new ArrayList<>();
        UnicastRemoteObject.exportObject(this,0);
        //mirror = this;
    }

    /*static void notifyObservers(String messageHeader, BusinessEntity entity) throws RemoteException{
        remoteSubjectDelegate.notifyObservers(new UpdateMessage(messageHeader, entity));
    }*/

    @Override
    public ArrayList<RemoteProjectInterface> getRemoteProjects() throws RemoteException {
        return remoteProjects;
    }

    @Override
    public void setRemoteProjects(ArrayList<RemoteProjectInterface> remoteProjects) throws RemoteException {
        this.remoteProjects = remoteProjects;
    }

    @Override
    public void addProject(RemoteProjectInterface project, String emailOfCreator) throws RemoteException {
        RemoteMemberInterface creator = getMember(emailOfCreator);
        project.getMembers().add(creator);
        this.remoteProjects.add(project);
        ServerModel.getInstance().getRemoteProjects().getRemoteProjects().add(project);
        ProjectDAO.getInstance().addProject(project.getName());
        ProjectDAO.getInstance().addParticipation(project.getName(), creator);
        if(emailOfCreator == null)
            return;
        project.addObserver(ServerModel.getInstance().getConnection(emailOfCreator));
        project.notifyObservers(MessageHeaders.CREATE, new Project(project));
    }

    @Override
    public void addProject(ProjectInterface project, String emailOfCreator) throws RemoteException {
        RemoteMemberInterface creator = getMember(emailOfCreator);
        RemoteProjectInterface remoteProject = new RProject(project);
        remoteProject.getMembers().add(creator);
        this.remoteProjects.add(remoteProject);
        ServerModel.getInstance().getRemoteProjects().getRemoteProjects().add(remoteProject);
        ProjectDAO.getInstance().addProject(project.getName());
        ProjectDAO.getInstance().addParticipation(project.getName(), creator);
        if(emailOfCreator == null)
            return;
        remoteProject.addObserver(ServerModel.getInstance().getConnection(emailOfCreator));
        remoteProject.notifyObservers(MessageHeaders.CREATE, new Project(remoteProject));
    }

    @Override
    public RemoteProjectInterface getProject(int projectIndex) throws RemoteException {
        return this.remoteProjects.get(projectIndex);
    }

    @Override
    public RemoteProjectInterface getProject(String projectName) throws RemoteException {
        for(RemoteProjectInterface remoteProject: remoteProjects){
            if(remoteProject.getName().equals(projectName))
                return remoteProject;
        }

        return null;
    }

    @Override
    public ArrayList<String> getProjectNames() throws RemoteException{
        ArrayList<String> projectNames = new ArrayList<>();
        for(RemoteProjectInterface project: remoteProjects){
            projectNames.add(project.getName());
        }

        return projectNames;
    }

    @Override
    public void removeProject(String projectName) throws RemoteException {
        for(RemoteProjectInterface project: remoteProjects){
            if(project.getName().equals(projectName)) {
                ProjectDAO.getInstance().deleteProject(project);
                project.notifyObservers(MessageHeaders.DELETE, new Project(project));
                remoteProjects.remove(project);
                ServerModel.getInstance().getRemoteProjects().getRemoteProjects().remove(project);
                break;
            }
        }

    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
        //RProjects.notifyObservers(this);
    }

    /*@Override
    public void addObserver(RemoteObserver<UpdateMessage> remoteObserver) throws RemoteException {
        remoteSubjectDelegate.addObserver(remoteObserver);
    }

    @Override
    public void deleteObserver(RemoteObserver<UpdateMessage> remoteObserver) throws RemoteException {
        remoteSubjectDelegate.deleteObserver(remoteObserver);
    }*/

}
