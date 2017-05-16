package client.view;

import shared.business_entities.Project;
import shared.business_entities.ProjectInterface;

import javax.swing.*;
import java.util.ArrayList;

public class Root extends Window {

	private static final long serialVersionUID = 1L;
	public static AbstractJIF projectFrame;
	public static AbstractJIF sprintFrame;
	public static AbstractJIF calendarFrame;
	public static AbstractJIF chatFrame;

	static String currentProjectName;


    @Override
    public void setEventHandlers() {

    }

    @Override
    public void initComponents() {
    	setLayout(null);
    	
		//Project frame
		projectFrame = new Projects();
		add(projectFrame);
		
		//Sprint frame
		sprintFrame = new Sprints();
		add(sprintFrame);
		
		//Calendar frame
		calendarFrame = new Calendar();
		add(calendarFrame);
		
		//Chat frame
		chatFrame = new Chat();
		add(chatFrame);
		
		//Background image
		JButton background = new JButton("");
		background.setBounds(0, 0, 2001, 1257);
		background.setContentAreaFilled(false);
		background.setBorderPainted(false);
		background.setBorder(null);
		background.setHorizontalAlignment(SwingConstants.RIGHT);
		background.setIcon(new ImageIcon(Root.class.getResource("/resources/planning_group_2000_logo.jpg")));
		background.setVerticalAlignment(SwingConstants.BOTTOM);
		add(background);

    }

    @Override
    public void loadData() {
        /*ArrayList<String> projectNames = controller.getProjectNames();
        for(String projectName: projectNames) {
			System.out.println("Project name: " + projectName);
			System.out.println("Task list: ");
			System.out.println(controller.getProjectFromModel(projectName).getTaskList());
		}*/
		projectFrame.loadData(controller.getProjectNames());
        if(currentProjectName == null || currentProjectName.equals("Projects"))
        	return;

        ProjectInterface project = controller.getProjectFromModel(currentProjectName);
        calendarFrame.loadData(project.getCalendar());
		chatFrame.loadData(project.getChat());
	}

    @Override
    public void showLogin() {
        view.setCurrentWindow(LOGIN);
    }

    @Override
    public void showRoot() {

    }
}
