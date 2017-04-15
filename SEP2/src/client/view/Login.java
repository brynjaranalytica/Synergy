package client.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle.ComponentPlacement;


import client.controller.ClientController;
import shared.User;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.awt.Toolkit;

public class Login extends Window{
	
	private static final long serialVersionUID = 1L;
	//private ClientController controller;
	private  JFrame frame;
	private  JTextField textField;
	private   JPasswordField passwordField;
	private  JButton btnLogIn;
	private  JLabel lblResetPassword;
	private JPasswordField pass1;
	private JPasswordField pass2;
	private JPanel passChange;

	/*public Login(ClientController controller){
		this.controller = controller;
		initComponents();
		createEvents();
	}*/




	//////////////////////////////////////////////////////////////////////////
	//All gui components:
	public void initComponents() {
		frame = new JFrame();
		frame.setResizable(false);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/resources/icon.png")));
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 420, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/resources/login.png")));
		
		textField = new JTextField();
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		
		JLabel lblUser = new JLabel("Email address:");
		
		JLabel lblPassword = new JLabel("Password:");
		
		btnLogIn = new JButton("Log in");
		
		lblResetPassword = new JLabel("Reset password");

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblNewLabel)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(99, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUser)
						.addComponent(lblPassword))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnLogIn)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblResetPassword))
						.addComponent(passwordField, Alignment.TRAILING)
						.addComponent(textField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
					.addGap(99))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addComponent(lblNewLabel)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUser))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnLogIn)
						.addComponent(lblResetPassword))
					.addContainerGap(45, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);

		//For entering new password
		pass1 = new  JPasswordField(16);
		pass2 = new  JPasswordField(16);
		
		passChange = new JPanel();
		passChange.add(new JLabel("Enter new password:"));
		passChange.add(pass1);
		passChange.add(Box.createHorizontalStrut(15)); // a spacer
		passChange.add(new JLabel("Reenter new password"));
		passChange.add(pass2);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////
	//All event handlers:
	public void setEventHandlers(){
		
		//Login button:
		btnLogIn.addActionListener(e -> {
			String userID = textField.getText();
			char[] pass = passwordField.getPassword();
			if (userID.isEmpty()||userID==null) {
				JOptionPane.showMessageDialog(null,"Email address is required - please try again", "Failed to log in", JOptionPane.OK_OPTION);
			} else if (!isValidEmailAddress(userID)) {
				JOptionPane.showMessageDialog(null,"Valid email address is required - please try again", "Failed to log in", JOptionPane.OK_OPTION);
			} else if (pass.length<6 || pass == null) {
				JOptionPane.showMessageDialog(null,"Password of minimum 6 characters is required.\nPlease try again", "Failed to log in", JOptionPane.OK_OPTION);
			} else {
				User user = controller.login(userID, pass);
				if (user != null){
					char[] passStored = user.getPass();
					if (passStored!=null && Arrays.equals(pass, passStored)){
						
						controller.displayProjects();
						//The user is now logged in and the projects he is in are displayed						
					} else {
						JOptionPane.showMessageDialog(null,"Log in failed.\nPlease check your email address and password and try again", "Failed to log in", JOptionPane.OK_OPTION);
					}
				} else {

					JOptionPane.showMessageDialog(null,"Log in failed.\nPlease check your email address and password and try again", "Failed to log in", JOptionPane.OK_OPTION);
				}
			}
		});
		
		//Reset password:
		lblResetPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				String userID =JOptionPane.showInputDialog(null, "Please enter your email address", "Reset password", 1);
				if (userID==null || userID.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Cannot be empty - try again", "Error", JOptionPane.OK_OPTION);
				} else  {
					String phone = controller.validateID(userID);
					if (phone  != null){
						String pin1 = Integer.toString(controller.getPIN(phone));
						String pin2 =JOptionPane.showInputDialog(null, "Please enter the PIN code sent to your phone", "Reset password", 1);
						//Check if the entered pin matches the generated pin:
						if (pin1!=null&&pin2!=null&pin1.equals(pin2)) {
							//Enter new password twice and validate:

						   JOptionPane.showConfirmDialog(null, passChange, "Please enter a new password", JOptionPane.OK_CANCEL_OPTION);
						  
						    	char[] newPass1 = pass1.getPassword();
						    	char[] newPass2 = pass2.getPassword();

						    	if (newPass1!=null&&newPass2!=null&&Arrays.equals(newPass1, newPass2)){
						    		//Passwords entered matches
						    		if (newPass1.length>=6){
							    		if(controller.savePass(userID, newPass1)){
							    			JOptionPane.showMessageDialog(null, "The new password has been stored", "Password has been reset", JOptionPane.INFORMATION_MESSAGE);							    			
							    		} else {
							    			JOptionPane.showMessageDialog(null, "Problem saving new password - please try again", "Password NOT reset", JOptionPane.WARNING_MESSAGE);
										}

						    		} else {
						    			JOptionPane.showMessageDialog(null, "Passwords must contain minimum 6 characters - please try again", "Error", JOptionPane.OK_OPTION);
									}
						    	} else {
						    		JOptionPane.showMessageDialog(null, "Entered passwords does not match - please try again", "Error", JOptionPane.OK_OPTION);
								}
						
						    }							
						} else {
							JOptionPane.showMessageDialog(null, "Email address not found - please try again", "Error", JOptionPane.OK_OPTION);
						}
					
				}
			}
		});
		
		//Close window:
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
	}
	
	//Helper method
	public static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}


	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLogin() {

	}

	@Override
	public void showMain() {
		view.setCurrentWindow(MAIN);
	}

}
