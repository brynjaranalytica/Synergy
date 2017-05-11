package client.model;

import client.controller.ClientController;
import shared.business_entities.Member;
import shared.business_entities.Project;
import shared.business_entities.ProjectInterface;
import shared.business_entities.TaskList;

import java.util.ArrayList;

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

    private void loadProjectIfNull(){
        if(realProject == null)
            realProject = ClientController.getProjectFromServer(name);
    }

}
