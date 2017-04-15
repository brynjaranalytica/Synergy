package client.view;

import javax.swing.JFrame;

import client.controller.ClientController;

public class View extends JFrame implements ViewInterface {

	private static final long serialVersionUID = 1L;
	private ClientController clientController;
	private ViewInterface window;
	private View view;
	
	public View(ClientController controller){
		clientController = controller;
		window = new Login(controller);
		view=new View(controller);
		view.setVisible(true);
	}
	@Override
	public void gotoLogin() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void gotoProjects() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void gotoProject() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void gotoSchedule() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void gotoToDo() {
		// TODO Auto-generated method stub
		
	}
}
