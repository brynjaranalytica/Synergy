package client.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class Projects extends AbstractJIF {

	private static final long serialVersionUID = 1L;
	private JLabel lblProjects;
	private JTree tree;
	
	public Projects() {
		setBounds(20, 20, 450, 300);
		initComponents();
		createEvents();
	}
	public void initComponents(){
		setBackground(Color.WHITE);
		lblProjects = new JLabel("Projects");
		lblProjects.setForeground(Color.GRAY);
		lblProjects.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblProjects.setBounds(10, 11, 200, 40);
		getContentPane().add(lblProjects);
		
		tree = new JTree();
		tree.setModel(new DefaultTreeModel(
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
		));
		tree.setRowHeight(22);
		tree.setFont(new Font("Raleway", Font.PLAIN, 16));
		tree.setBounds(10, 60, 322, 694);
		getContentPane().add(tree);
		
	}
	public void createEvents(){
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Root.projectFrame.setVisible(false);
				Root.sprintFrame.setVisible(true);
			}
		});
	}
}
