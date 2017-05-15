package client.model;

import client.controller.ClientController;
import shared.business_entities.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 4/17/2017.
 */
public class ProxyProject implements ProjectInterface {
    private Project realProject;
    private String name;

    ProxyProject(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ArrayList<Member> getMembers() {
        loadProjectIfNull();
        return realProject.getMembers();
    }

    @Override
    public void setName(String name) {
        loadProjectIfNull();
        realProject.setName(name);
    }

    @Override
    public void setMembers(ArrayList<Member> members) {
        loadProjectIfNull();
        realProject.setMembers(members);
    }

    @Override
    public TaskList getTaskList() {
        loadProjectIfNull();
        return realProject.getTaskList();
    }

    @Override
    public void setTaskList(TaskList taskList) {
        loadProjectIfNull();
        realProject.setTaskList(taskList);
    }

    @Override
    public void addTask(String task) {
        loadProjectIfNull();
        realProject.addTask(task);
    }

    @Override
    public void addMember(Member member) {
        loadProjectIfNull();
        realProject.addMember(member);
    }

    @Override
    public Member getMember(int index) {
        loadProjectIfNull();
        return realProject.getMember(index);
    }

    @Override
    public Chat getChat() {
        loadProjectIfNull();
        return realProject.getChat();
    }

    @Override
    public Calendar getCalendar() {
        loadProjectIfNull();
        return realProject.getCalendar();
    }

    @Override
    public void setChat(Chat chat) {
        loadProjectIfNull();
        realProject.setChat(chat);
    }

    @Override
    public void setCalendar(Calendar calendar) {
        loadProjectIfNull();
        realProject.setCalendar(calendar);
    }

    @Override
    public void addMessage(String message) {
        loadProjectIfNull();
        realProject.addMessage(message);
    }

    @Override
    public void addMemo(Memo memo) {
        loadProjectIfNull();
        this.realProject.addMemo(memo);
    }

    @Override
    public void removeMemo(Date date) {
        loadProjectIfNull();
        this.realProject.removeMemo(date);
    }

    @Override
    public ArrayList<Memo> getMemos() {
        loadProjectIfNull();
        return realProject.getMemos();
    }

    @Override
    public Memo getMemo(Date date) {
        loadProjectIfNull();
        return realProject.getMemo(date);
    }

    private void loadProjectIfNull(){
        if(realProject == null)
            realProject = ClientController.getProjectFromServer(name);
    }

    void setRealProject(Project updatedProject){
        this.realProject = updatedProject;
    }

}
