package client.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class Chat extends AbstractJIF {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JLabel lblChat;
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
		
		list = new JList<String>();
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
		
	}
}
