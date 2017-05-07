package client.model;

import client.controller.ClientController;
import shared.Project;
import shared.User;

import java.util.ArrayList;

public class ClientModel {
	private User user;
	private ArrayList<Project> projects;
	private ClientController controller;
	
	public ClientModel(ClientController controller){
		this.controller = controller;
		user = null;
		projects = new ArrayList<>();
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public Project getProject(String projectName){

		return null;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public User getUser(){
		return user;
	}

}
