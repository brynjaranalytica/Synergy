package client.view;

import client.controller.ClientController;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;

public class Projects extends AbstractJIF {

	private static final long serialVersionUID = 1L;
	private JLabel lblProjects;
	private JTree tree;
	private JButton btnNewProject;
	private JButton btnDelProject;
	private JButton btnAddUser;
	private JButton btnDelUser;
	private JList listUsers;
	private DefaultTreeModel treeModel;

	
	public Projects() {
		setBounds(20, 20, 500, 300);
		initComponents();
		createEvents();
	}

	@Override
	public void loadData(Object object) {
		ArrayList<String> projectNames = (ArrayList<String>) object;
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		root.removeAllChildren();

		for(String projectName: projectNames){
			root.add(new DefaultMutableTreeNode(projectName));
		}
	}

	public void initComponents(){
		setBackground(Color.WHITE);
		lblProjects = new JLabel("Projects");
		lblProjects.setForeground(Color.GRAY);
		lblProjects.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblProjects.setBounds(10, 11, 200, 40);
		getContentPane().add(lblProjects);
		
		tree = new JTree();
		treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Projects"));
		tree.setModel(treeModel);
		/*tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Projects") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Project 1");
						node_1.add(new DefaultMutableTreeNode(""));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Project 2");
						node_1.add(new DefaultMutableTreeNode(""));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Project 3");
						node_1.add(new DefaultMutableTreeNode(""));
					add(node_1);
				}
			}
		));*/
		tree.setRowHeight(22);
		tree.setFont(new Font("Raleway", Font.PLAIN, 16));
		tree.setBounds(10, 60, 322, 694);
		getContentPane().add(tree);
		
		btnNewProject = new JButton("New project");
		btnNewProject.setBounds(342, 62, 110, 23);
		getContentPane().add(btnNewProject);
		
		btnDelProject = new JButton("Delete project");
		btnDelProject.setBounds(342, 96, 110, 23);
		getContentPane().add(btnDelProject);
		
		btnAddUser = new JButton("Add user");
		btnAddUser.setBounds(342, 130, 110, 23);
		getContentPane().add(btnAddUser);
		
		btnDelUser = new JButton("Delete user");
		btnDelUser.setBounds(342, 164, 110, 23);
		getContentPane().add(btnDelUser);
		
		listUsers = new JList();
		listUsers.setBounds(342, 152, 150, 160);
		getContentPane().add(listUsers);
		
	}

	public void createEvents(){
		/*tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Root.projectFrame.setVisible(false);
				Root.sprintFrame.setVisible(true);
				//int selectedProjectName = tree.getSel
				Root.chatFrame.loadData();
			}
		});*/

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {

				DefaultMutableTreeNode selectedNode =
						(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				String selectedProjectName = (String) selectedNode.getUserObject();
				Root.currentProjectName = selectedProjectName;
                if(selectedProjectName.equals("Projects"))
                    return;
				Root.chatFrame.loadData(ClientController.getInstance().getProjectFromModel(selectedProjectName).getChat());
				Root.calendarFrame.loadData(ClientController.getInstance().getProjectFromModel(selectedProjectName).getCalendar());
			}
		});

		btnNewProject.addActionListener(e -> {
			
		});

		btnDelProject.addActionListener(e -> {
			
		});
		
	}

}
