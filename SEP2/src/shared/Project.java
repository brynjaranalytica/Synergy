package shared;

import java.io.Serializable;

public class Project implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	public Project(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
}
