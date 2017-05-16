package shared.business_entities;

import shared.remote_business_interfaces.RemoteProjectInterface;
import shared.remote_business_interfaces.RemoteProjectsInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/11/2017.
 */
public class Projects implements BusinessEntity {
    private ArrayList<ProjectInterface> projects;
    private String name;// COMPANY/ORGANIZATION NAME

    public Projects(RemoteProjectsInterface root) throws RemoteException {
        this.projects = new ArrayList<>();
        this.name = root.getName();

        ArrayList<RemoteProjectInterface> remoteProjects = root.getRemoteProjects();
        for(RemoteProjectInterface remoteProject: remoteProjects)
            this.projects.add(new Project(remoteProject));

    }

    public Projects(String name) {
        projects = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProjectInterface> getProjectList() {
        return projects;
    }

    public void setProjects(ArrayList<ProjectInterface> projects) {
        this.projects = projects;
    }

    public void addProject(ProjectInterface project) {
        this.projects.add(project);
    }

    public ProjectInterface getProject(int projectIndex){
        return projects.get(projectIndex);
    }

    public ArrayList<String> getProjectNames(){
        ArrayList<String> projectNames = new ArrayList<>();
        for(ProjectInterface project: projects){
            projectNames.add(project.getName());
        }

        return projectNames;
    }
}
