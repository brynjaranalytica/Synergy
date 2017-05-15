package client.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Projects extends AbstractJIF {

	private static final long serialVersionUID = 1L;
	private JLabel lblProjects;
	private JTree tree;
	private JButton btnNewProject;
	private JButton btnDelProject;
	private JButton btnAddUser;
	private JButton btnDelUser;
	private JList listUsers;
	
	public Projects() {
		setBounds(20, 20, 450, 300);
		initComponents();
		createEvents();
	}

	public void initComponents(){
		setBackground(Color.WHITE);
		lblProjects = new JLabel("RProjects");
		lblProjects.setForeground(Color.GRAY);
		lblProjects.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblProjects.setBounds(10, 11, 200, 40);
		getContentPane().add(lblProjects);
		
		tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("RProjects") {
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
		));
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
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Root.projectFrame.setVisible(false);
				Root.sprintFrame.setVisible(true);
			}
		});

		btnNewProject.addActionListener(e -> {
			
		});

		btnDelProject.addActionListener(e -> {
			
		});
		
	}
}
