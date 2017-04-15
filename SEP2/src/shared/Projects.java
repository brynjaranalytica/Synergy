package shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Projects implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Project> projects;
	public Projects(){
		projects = new ArrayList<>();
	}
	public void addProject(Project project){
		projects.add(project);
	}
	public Project getProject(Project project){
		for (Project p : projects) {
			if (p.getName().equals(project.getName())){
				return p;
			}
		}
		return null;
	}
	public Project getProject(String str){
		for (Project p : projects) {
			if (p.getName().equals(str)){
				return p;
			}
		}
		return null;
	}
}
