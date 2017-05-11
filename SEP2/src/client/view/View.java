package client.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static client.view.Window.view;


public class View extends JFrame implements ViewInterface{

	private static final long serialVersionUID = 1L;
	private final JDesktopPane desktopPane = new JDesktopPane();
    private Window currentWindow;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnEdit;
	private JMenu mnWindow;
	private JMenuItem mntmLogOut;
	private JMenuItem mntmNewProject;
	private JMenuItem mntmExit;
	private JMenuItem mntmProjects;
	private JMenuItem mntmSprints;
	private JMenuItem mntmCalendar;
	private JMenuItem mntmChat;


    public View() {
    	setTitle("Synergy\u00AE    Project Management System");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
            	if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit and close?")==0){
					System.exit(0);				
				}
            }
        });
        setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/resources/icon.png")));
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       setLoginSize();
       setVisible(true);


        desktopPane.setLayout(new CardLayout(0,0));
        getContentPane().add(desktopPane, BorderLayout.CENTER);
        setCurrentWindow(LOGIN);
        view = this;
    }
    
    public void setLoginSize(){
    	setBounds(100, 100, 420, 300);
    	 setLocationRelativeTo(null);
    }
    
    public void setFullScreen(){
    	setExtendedState(MAXIMIZED_BOTH);
    	initMenu();
    	createMenuEvents();
    	menuBar.setVisible(true);
    }


    @Override
    public void showLogin() {
    	setLoginSize();
    	menuBar.setVisible(false);
        currentWindow.showLogin();
    }

    @Override
    public void showRoot() {
    	setFullScreen();
        currentWindow.showRoot();
    }

    @Override
    public void setEventHandlers() {
        currentWindow.setEventHandlers();
    }

    @Override
    public void initComponents() {
        currentWindow.initComponents();
    }

    @Override
    public void loadData() {
        currentWindow.loadData();
    }


    public void setCurrentWindow(Window window) {
        if(desktopPane.getComponentCount() == 0) {
            window.loadData();
            window.setVisible(true);
            desktopPane.add(window);
            currentWindow = window;
        }
        else{
            desktopPane.getComponent(0).setVisible(false);
            desktopPane.removeAll();
            window.loadData();
            window.setVisible(true);
            desktopPane.add(window);
            currentWindow = window;
        }
    }
    
    public void initMenu(){
		//Menu bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnEdit = new JMenu("Edit");
		mnWindow = new JMenu("Window");
		
		menuBar.add(mnFile);		
		menuBar.add(mnEdit);
		menuBar.add(mnWindow);
		
		mntmLogOut = new JMenuItem("Log out");
		mntmNewProject = new JMenuItem("New project");
		mntmExit = new JMenuItem("Exit");
		
		mntmProjects = new JMenuItem("RProjects");
		mntmSprints = new JMenuItem("Sprints");
		mntmChat = new JMenuItem("Group chat");
		mntmCalendar = new JMenuItem("Calendar");
		mnFile.add(mntmNewProject);
		mnFile.add(mntmLogOut);
		mnWindow.add(mntmProjects);
		mnWindow.add(mntmSprints);
		mnWindow.add(mntmChat);
		mnWindow.add(mntmCalendar);
		
		mnFile.add(mntmExit);
    }
    
	public void createMenuEvents(){
		mntmCalendar.addActionListener(e -> { Root.calendarFrame.setVisible(true); });
		mntmSprints.addActionListener(e -> { Root.sprintFrame.setVisible(true); Root.projectFrame.setVisible(false);});
		mntmChat.addActionListener(e -> { Root.chatFrame.setVisible(true); });
		mntmProjects.addActionListener(e -> { Root.projectFrame.setVisible(true); Root.sprintFrame.setVisible(false);});
		mntmLogOut.addActionListener(e -> { showLogin(); });
		mntmExit.addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit and close?")==0){
				System.exit(0);				
			}
		});
	}

}
