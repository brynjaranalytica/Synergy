package server.remote_business_enitities;


import shared.MessageHeaders;
import shared.business_entities.Member;
import shared.business_entities.Project;
import shared.business_entities.ProjectInterface;
import shared.remote_business_interfaces.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public class RProject implements RemoteProjectInterface {
    private String name;
    private ArrayList<RemoteMemberInterface> members;
    private RemoteTaskListInterface taskList;
    private RemoteCalendarInterface calendar;
    private RemoteChatInterface chat;

    public RProject(ProjectInterface project)throws RemoteException{
        this.name = project.getName();
        this.members = new ArrayList<>();

        // Chat and calendar converting is not implemented

        ArrayList<Member> members = project.getMembers();
        for(Member member: members){
            this.members.add(new RMember(member));
        }

        this.taskList  = new RTaskList(project.getTaskList());
        UnicastRemoteObject.exportObject(this,0);
    }

    public RProject(String name) throws RemoteException {
        this.members = new ArrayList<>();
        this.taskList = new RTaskList();
        this.calendar = new RCalendar();
        this.chat = new RChat();
        this.name = name;
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public ArrayList<RemoteMemberInterface> getMembers() throws RemoteException {
        return members;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
        RProjects.notifyObservers(MessageHeaders.UPDATE, new Project(this));
    }

    @Override
    public void setMembers(ArrayList<RemoteMemberInterface> members) throws RemoteException {
        this.members = members;
    }

    @Override
    public RemoteTaskListInterface getTaskList() throws RemoteException {
        return this.taskList;
    }

    @Override
    public void setTaskList(RemoteTaskListInterface taskList) throws RemoteException {
        this.taskList = taskList;
    }

    @Override
    public void addTask(String task) throws RemoteException {
        this.taskList.addTask(task);
        RProjects.notifyObservers(MessageHeaders.UPDATE, new Project(this));
    }

    @Override
    public void addMember(RemoteMemberInterface member) throws RemoteException {
        this.members.add(member);
        RProjects.notifyObservers(MessageHeaders.UPDATE, new Project(this));
    }

    @Override
    public RemoteMemberInterface getMember(int index) throws RemoteException {
        return this.members.get(index);
    }

    @Override
    public String toString() {
        try {
            return name + ": " + taskList.getTasks().toString() + ". Number of members: " + members.size() + ".";
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

}
