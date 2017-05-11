package shared.business_entities;



import shared.remote_business_interfaces.RemoteMemberInterface;
import shared.remote_business_interfaces.RemoteProjectInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * Created by lenovo on 4/11/2017.
 */
public class Project implements ProjectInterface, BusinessEntity  {
    private String name;
    private ArrayList<Member> members;
    private TaskList taskList;

    public Project(RemoteProjectInterface remoteProject) throws RemoteException{
        this.members = new ArrayList<Member>();
        this.taskList = new TaskList(remoteProject.getTaskList());
        this.name = remoteProject.getName();

        ArrayList<RemoteMemberInterface> remoteMembers = remoteProject.getMembers();
        for(RemoteMemberInterface remoteMember: remoteMembers)
            this.members.add(new Member(remoteMember));


    }

    public Project(String name) {
        this.name = name;
        this.members = new ArrayList<Member>();
        this.taskList = new TaskList();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public void addTask(String task){
        this.taskList.addTask(task);
    }

    public void addMember(Member member){
        this.members.add(member);
    }

    @Override
    public String toString() {
        return name + ": " + taskList.getTasks().toString() + ". Number of members: " + members.size() + ".";
    }

    public Member getMember(int index){
         return this.members.get(index);
    }

}
