package shared.business_entities;



import client.controller.ClientController;
import shared.remote_business_interfaces.RemoteMemberInterface;
import shared.remote_business_interfaces.RemoteProjectInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by lenovo on 4/11/2017.
 */
public class Project implements ProjectInterface {
    private String name;
    private ArrayList<Member> members;
    private TaskList taskList;
    private Chat chat;
    private Calendar calendar;

    public Project(RemoteProjectInterface remoteProject) throws RemoteException{
        this.members = new ArrayList<Member>();
        this.taskList = new TaskList(remoteProject.getTaskList());
        this.name = remoteProject.getName();
        this.chat = new Chat(remoteProject.getChat());
        this.calendar = new Calendar(remoteProject.getCalendar());

        ArrayList<RemoteMemberInterface> remoteMembers = remoteProject.getMembers();
        for(RemoteMemberInterface remoteMember: remoteMembers)
            this.members.add(new Member(remoteMember));


    }

    public Project(String name) {
        this.name = name;
        this.members = new ArrayList<Member>();
        this.taskList = new TaskList();
        this.chat = new Chat();
        this.calendar = new Calendar();
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

    public void addMessage(String message) {
        this.chat.addMessage(message);
    }

    @Override
    public void addMemo(Memo memo) {
        this.calendar.addMemo(memo);
    }

    @Override
    public void removeMemo(Date date) {
        this.calendar.removeMemo(date);
    }

    @Override
    public ArrayList<Memo> getMemos() {
        return this.calendar.getMemos();
    }

    @Override
    public Memo getMemo(Date date) {
        return this.calendar.getMemo(date);
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

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

}
