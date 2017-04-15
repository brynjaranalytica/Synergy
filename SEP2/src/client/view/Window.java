package client.view;

import javax.swing.JPanel;

public abstract class Window extends JPanel implements ViewInterface {

	private static final long serialVersionUID = 1L;
	public Window(){
		initComponents();
		createEvents();
		loadData();
	}
	public abstract void loadData() ;
	public abstract void createEvents();
	public abstract void initComponents();
	
}
