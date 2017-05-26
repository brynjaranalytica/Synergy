package client.view.internal_frames;

import client.controller.ClientController;
import client.view.windows.Root;
import shared.business_entities.Member;
import shared.business_entities.Project;
import shared.business_entities.ProjectInterface;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class Projects extends AbstractJIF {

    private DefaultMutableTreeNode rootNode;
    private static final long serialVersionUID = 1L;
    private JLabel lblProjects;
    private JTree tree;
    private JButton btnNewProject;
    private JButton btnDelProject;
    private JButton btnAddUser;
    private JButton btnDelUser;
    private JList listUsers;
    private DefaultTreeModel treeModel;
    private JButton btnAddUserToProject;
    private JPanel panelAddUser;
    private JButton btnCancelAddUserToProject;
    private JPanel panelAddProject;
    private JTextField textFieldAddProject;
    private JButton btnAddProjectDropDown;
    private JButton btnCancelAddProject;
    private DefaultListModel<String> listUsersModel;

    @Override
    public void loadData(Object object) {
        ClientController controller = ClientController.getInstance();
        shared.business_entities.Projects projects = (shared.business_entities.Projects) object;
        ArrayList<String> projectNames = controller.getProjectNames();
        ArrayList<Member> organizationMembers = projects.getMembers();
        listUsersModel.clear();
        for(Member member: organizationMembers){
            listUsersModel.addElement(member.getName());
        }
        rootNode.removeAllChildren();

        for (int i = 0; i < projectNames.size(); i++) {
            DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(projectNames.get(i));
            treeModel.insertNodeInto(projectNode, rootNode, i);
            ProjectInterface project = controller.getProjectFromModel(projectNames.get(i));
            ArrayList<Member> members = project.getMembers();
            for(int j = 0; j < members.size(); j++){
                treeModel.insertNodeInto(new DefaultMutableTreeNode(members.get(j).getName()), projectNode, j);
            }
        }

        treeModel.reload(rootNode);
    }

    @Override
    public void clear() {

    }

    public Projects() {
        setBounds(20, 20, 500, 375);
        initComponents();
        createEvents();
    }

    public void initComponents() {
        setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        lblProjects = new JLabel("Projects");
        lblProjects.setBounds(10, 11, 200, 40);
        lblProjects.setForeground(Color.GRAY);
        lblProjects.setFont(new Font("Raleway", Font.PLAIN, 30));
        getContentPane().add(lblProjects);

        rootNode = new DefaultMutableTreeNode("Projects");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setBounds(10, 60, 322, 694);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        //tree.setShowsRootHandles(true);

        tree.setRowHeight(22);
        tree.setFont(new Font("Raleway", Font.PLAIN, 16));

        /*JScrollPane treeView = new JScrollPane(tree);
        treeView.setLayout(new ScrollPaneLayout());
        treeView.setVisible(true);*/
        getContentPane().add(tree);

        panelAddUser = new JPanel();
        panelAddUser.setVisible(false);

        panelAddProject = new JPanel();
        panelAddProject.setVisible(false);
        panelAddProject.setBackground(SystemColor.textHighlight);
        panelAddProject.setBounds(342, 62, 110, 91);
        getContentPane().add(panelAddProject);
        panelAddProject.setLayout(null);

        textFieldAddProject = new JTextField();
        textFieldAddProject.setForeground(SystemColor.text);
        textFieldAddProject.setBackground(SystemColor.textHighlight);
        textFieldAddProject.setBounds(5, 20, 100, 20);
        panelAddProject.add(textFieldAddProject);
        textFieldAddProject.setColumns(10);

        btnAddProjectDropDown = new JButton("Add project");
        btnAddProjectDropDown.setBounds(0, 45, 110, 23);
        panelAddProject.add(btnAddProjectDropDown);

        btnCancelAddProject = new JButton("Cancel");
        btnCancelAddProject.setBounds(0, 68, 110, 23);
        panelAddProject.add(btnCancelAddProject);

        JLabel lblProjectName = new JLabel("Project name:");
        lblProjectName.setForeground(SystemColor.text);
        lblProjectName.setBounds(5, 5, 100, 15);
        panelAddProject.add(lblProjectName);

        panelAddUser.setBounds(342, 130, 110, 210);
        getContentPane().add(panelAddUser);
        panelAddUser.setLayout(null);

        listUsers = new JList();
        listUsers.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), null));
        listUsers.setForeground(SystemColor.text);
        listUsers.setBackground(SystemColor.textHighlight);
        listUsers.setBounds(0, 0, 110, 160);
        listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelAddUser.add(listUsers);
        listUsersModel = new DefaultListModel<String>();
        listUsers.setModel(listUsersModel);

        btnAddUserToProject = new JButton("Add user");
        btnAddUserToProject.setBounds(0, 160, 110, 23);
        panelAddUser.add(btnAddUserToProject);

        btnCancelAddUserToProject = new JButton("Cancel");
        btnCancelAddUserToProject.setBounds(0, 185, 110, 23);
        panelAddUser.add(btnCancelAddUserToProject);

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
                if(!rootNode.isNodeChild(selectedNode))
                    return;

                String selectedProjectName = (String) selectedNode.getUserObject();
                Root.currentProjectName = selectedProjectName;
                lblProjects.setText(Root.currentProjectName);
                if (Root.currentProjectName.equals("Projects"))
                    return;

                Root.chatFrame.loadData(ClientController.getInstance().getProjectFromModel(selectedProjectName).getChat());
                Root.calendarFrame.loadData(ClientController.getInstance().getProjectFromModel(selectedProjectName).getCalendar());
            }
        });

        btnDelProject.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (Root.currentProjectName == null || Root.currentProjectName.equals("Projects") || !rootNode.isNodeChild(selectedNode)) {
                JOptionPane.showMessageDialog(null, "You have to select one of the existing projects.");
                return;
            }

            String selectedProjectName = (String) selectedNode.getUserObject();
            Root.currentProjectName = "Projects";
            ClientController.getInstance().deleteProject(selectedProjectName);
            Root.calendarFrame.clear();
            Root.chatFrame.clear();
            tree.expandPath(tree.getPathForRow(0));
        });

        btnNewProject.addActionListener(e ->
        {
            textFieldAddProject.setText("");
            btnNewProject.setVisible(false);
            btnDelProject.setVisible(false);
            btnAddUser.setVisible(false);
            btnDelUser.setVisible(false);
            panelAddProject.setVisible(true);
        });

        btnAddProjectDropDown.addActionListener(e ->
        {
            panelAddProject.setVisible(false);
            btnNewProject.setVisible(true);
            btnDelProject.setVisible(true);
            btnAddUser.setVisible(true);
            btnDelUser.setVisible(true);
            //add new project code here
            if(textFieldAddProject.getText().equals("") || textFieldAddProject.getText() == null)
                return;
            String projectName = textFieldAddProject.getText();
            ClientController.getInstance().addProject(new Project(projectName));
            tree.expandPath(tree.getPathForRow(0));
        });

        btnCancelAddProject.addActionListener(e ->
        {
            panelAddProject.setVisible(false);
            btnNewProject.setVisible(true);
            btnDelProject.setVisible(true);
            btnAddUser.setVisible(true);
            btnDelUser.setVisible(true);
        });

        btnAddUser.addActionListener(e ->
        {
            panelAddUser.setVisible(true);
            btnAddUser.setVisible(false);


        });

        btnAddUserToProject.addActionListener(e ->
        {
            panelAddUser.setVisible(false);
            btnAddUser.setVisible(true);
            //add selected user to selected project code here
            int indexOfSelectedMember = listUsers.getSelectedIndex();
            if(indexOfSelectedMember == -1)
                return;
            if(Root.currentProjectName.equals("Projects") || Root.currentProjectName == null) {
                JOptionPane.showMessageDialog(null, "You have to select one of the existing projects first.");
                return;
            }

            if(!ClientController.getInstance().addMember(indexOfSelectedMember))
                JOptionPane.showMessageDialog(null ,"Selected member already is a part of the project.");

            tree.expandPath(tree.getPathForRow(0));
        });

        btnCancelAddUserToProject.addActionListener(e ->
        {
            panelAddUser.setVisible(false);
            btnAddUser.setVisible(true);
        });

        btnDelUser.addActionListener(e ->
        {
            //delete selected user from selected project
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (Root.currentProjectName == null || Root.currentProjectName.equals("Projects")) {
                JOptionPane.showMessageDialog(null, "You have to select one of the existing projects.");
                return;
            }

            if(rootNode.isNodeChild(selectedNode)) {
                JOptionPane.showMessageDialog(null, "You have to select one of the members of selected project.");
                return;
            }

            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
            if(!String.valueOf(parentNode.getUserObject()).equals(Root.currentProjectName)) {
                JOptionPane.showMessageDialog(null, "Selected project and selected member has to be from the same project.");
                return;
            }

            int indexOfSelectedMember = parentNode.getIndex(selectedNode);
            ClientController.getInstance().removeMember(indexOfSelectedMember);
            tree.expandPath(tree.getPathForRow(0));
        });

    }


}
