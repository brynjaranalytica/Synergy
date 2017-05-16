package client.view;

import client.controller.ClientController;
import server.remote_business_enitities.RProject;
import shared.business_entities.Project;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.rmi.RemoteException;
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
    private DefaultMutableTreeNode rootNode;
    private int newProjectIndex = 1;


    public Projects() {
        setBounds(20, 20, 450, 300);
        initComponents();
        createEvents();
    }

    @Override
    public void loadData(Object object) {
        ArrayList<String> projectNames = (ArrayList<String>) object;
        //DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        rootNode.removeAllChildren();
        //treeModel.reload();


        for (int i = 0; i < projectNames.size(); i++) {
            treeModel.insertNodeInto(new DefaultMutableTreeNode(projectNames.get(i)), rootNode, i);
        }
        treeModel.reload();
    }

    /*private DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            //There is no selection. Default to the root node.
            parentNode = (DefaultMutableTreeNode) treeModel.getRoot();
        } else {
            parentNode = (DefaultMutableTreeNode)
                    (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    private DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode =
                new DefaultMutableTreeNode(child);
        treeModel.insertNodeInto(childNode, parent,
                parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }*/

    public void initComponents() {
        setBackground(Color.WHITE);
        lblProjects = new JLabel("Projects");
        lblProjects.setForeground(Color.GRAY);
        lblProjects.setFont(new Font("Raleway", Font.PLAIN, 30));
        lblProjects.setBounds(10, 11, 200, 40);
        getContentPane().add(lblProjects);

        rootNode = new DefaultMutableTreeNode("Projects");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
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

    public void createEvents() {
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
                        (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                String selectedProjectName = (String) selectedNode.getUserObject();
                Root.currentProjectName = selectedProjectName;
                if (selectedProjectName.equals("Projects"))
                    return;
                Root.chatFrame.loadData(ClientController.getInstance().getProjectFromModel(selectedProjectName).getChat());
                Root.calendarFrame.loadData(ClientController.getInstance().getProjectFromModel(selectedProjectName).getCalendar());
            }
        });

        btnNewProject.addActionListener(e -> {
            String projectName = /*txtFieldProjectName.getText()*/"New project " + newProjectIndex++;
            ClientController.getInstance().addProject(new Project(projectName));

        });

        btnDelProject.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            String selectedProjectName = (String) selectedNode.getUserObject();
            ClientController.getInstance().deleteProject(selectedProjectName);
        });

    }

}
