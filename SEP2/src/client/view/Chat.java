package client.view;

import client.controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Chat extends AbstractJIF {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JLabel lblChat;
	DefaultListModel<String> model;
	private JList<String> list;
	private JButton btnSend;

	public Chat() {
		setBounds(20, 400, 300, 400);
		initComponents();
		createEvents();
	}

	public void initComponents(){
		lblChat = new JLabel("Group chat");
		lblChat.setForeground(Color.GRAY);
		lblChat.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblChat.setBounds(10, 11, 200, 35);
		getContentPane().add(lblChat);

		model = new DefaultListModel<>();
		list = new JList<String>(model);
		list.setAutoscrolls(true);
		
		list.setBounds(10, 59, 264, 223);
		
		getContentPane().add(list);
		
		textField = new JTextField();
		textField.setBounds(10, 287, 264, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(98, 325, 89, 23);
		getContentPane().add(btnSend);
	}

	public void createEvents(){


		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(Root.currentProjectName == null || Root.currentProjectName.equals("Projects")){
					JOptionPane.showMessageDialog(null, "You have to select one of the projects first.");
					return;
				}

				if(textField.getText().equals("") || textField.getText() == null)
					return;

				String message = textField.getText();
				ClientController.getInstance().sendChatMessage(Root.currentProjectName, message);
				textField.setText("");
			}
		});
	}

	public void loadData(Object object){
		this.model.clear();
		ArrayList<String> messages =  ((shared.business_entities.Chat)object).getListOfMessages();

		for (String message: messages)
			this.model.addElement(message);
	}

}
