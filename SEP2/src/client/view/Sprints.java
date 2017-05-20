package client.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class Sprints extends AbstractJIF {


	private static final long serialVersionUID = 1L;
	private JLabel lblProgress;
	private JProgressBar progressBar;
	private JProgressBar progressBar_1;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private DatePicker datePicker;

	public Sprints() {
		
		setVisible(false);
		
		setBounds(20, 20, 450, 350);
		setBackground(Color.WHITE);
		initComponents();
		createEvents();

	}

	@Override
	public void loadData(Object object) {

	}

	@Override
	public void clear() {

	}

	public void initComponents(){
		
		datePicker = new DatePicker();
		
		JLabel lblSprints = new JLabel("Sprints");
		lblSprints.setForeground(Color.GRAY);
		lblSprints.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblSprints.setBounds(10, 11, 154, 27);
		getContentPane().add(lblSprints);
		
		lblProgress = new JLabel("Progress:");
		lblProgress.setBounds(263, 24, 60, 14);
		getContentPane().add(lblProgress);
		
		progressBar = new JProgressBar();
		progressBar.setString("Planned 40%");
		progressBar.setStringPainted(true);
		progressBar.setValue(40);
		progressBar.setBounds(263, 48, 146, 14);
		getContentPane().add(progressBar);
		
		progressBar_1 = new JProgressBar();
		progressBar_1.setString("Actual 30%");
		progressBar_1.setStringPainted(true);
		progressBar_1.setValue(30);
		progressBar_1.setBounds(263, 73, 146, 14);
		getContentPane().add(progressBar_1);
		
		btnNewButton = new JButton("Add sprint");
		btnNewButton.setBounds(263, 122, 146, 23);
		getContentPane().add(btnNewButton);
		
		btnNewButton_1 = new JButton("Delete sprint");
		btnNewButton_1.setBounds(263, 156, 146, 23);
		getContentPane().add(btnNewButton_1);
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Project 1") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Sprint 1");
						node_1.add(new DefaultMutableTreeNode("Implement this"));
						node_1.add(new DefaultMutableTreeNode("Implement that"));
						node_1.add(new DefaultMutableTreeNode("Code this"));
						node_1.add(new DefaultMutableTreeNode("Code that"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Sprint 2");
						node_1.add(new DefaultMutableTreeNode("Program this"));
						node_1.add(new DefaultMutableTreeNode("Program that"));
						node_1.add(new DefaultMutableTreeNode("Make save feature"));
						node_1.add(new DefaultMutableTreeNode("Make calendar"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Sprint 3");
						node_1.add(new DefaultMutableTreeNode("Only tech stuff"));
						node_1.add(new DefaultMutableTreeNode("Implement GUI"));
						node_1.add(new DefaultMutableTreeNode("Eat pizza"));
						node_1.add(new DefaultMutableTreeNode("Drink beer"));
					add(node_1);
				}
			}
		));
		tree.setRowHeight(22);
		tree.setFont(new Font("Raleway", Font.PLAIN, 16));
		tree.setBounds(10, 60, 301, 700);
		getContentPane().add(tree);
	}
	
	public void createEvents(){
		btnNewButton.addActionListener(e -> {
			datePicker.setVisible(true);
		});
	}

}
