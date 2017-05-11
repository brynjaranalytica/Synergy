package client.model;


import shared.User;
import shared.business_entities.ProjectInterface;
import shared.business_entities.Projects;

import java.util.ArrayList;

public class ClientModel {
	private User user;
	private Projects projects;
	//private ClientController controller;
	
	public ClientModel(){
		//this.controller = controller;
		user = null;
		projects = new Projects("company name not loaded yet");
	}

	public Projects getProjects() {
		return this.projects;
	}

	public ProjectInterface getProject(String projectName){
		for(ProjectInterface project: projects.getProjectList()){
			if(project.getName().equals(projectName))
				return project;
		}

		return null;
	}

	public void setProjects(Projects projects) {
		this.projects = projects;
	}

	public void initProxyProjects(ArrayList<String> projectNames) {
		ArrayList<ProjectInterface> projects = this.projects.getProjectList();
		for(String projectName: projectNames){
			projects.add(new ProxyProject(projectName));
		}
	}

	public void setOrganizationName(String name){
		this.projects.setName(name);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser(){
		return user;
	}

}
