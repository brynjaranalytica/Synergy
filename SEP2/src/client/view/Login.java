package client.view;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Box;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import shared.User;
import utility.Validator;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class Login extends Window{	
	
	private static final long serialVersionUID = 1L;
	private  JTextField textFieldEmailAddress;
	private   JPasswordField passwordField;
	private  JButton btnLogIn;
	private  JLabel lblResetPassword;
	private JPasswordField pass1;
	private JPasswordField pass2;
	private JPanel passChange;
	private JLabel lblLogo;
	private JLabel lblPassword;
	private JLabel lblUser;

	public Login() {
	}


	public void initComponents() {

		lblLogo = new JLabel("");
		lblLogo.setBounds(0, 0, 400, 78);
		lblLogo.setIcon(new ImageIcon(Login.class.getResource("/resources/login.png")));
		setBackground(Color.WHITE);
		textFieldEmailAddress = new JTextField();
		textFieldEmailAddress.setBounds(167, 117, 150, 20);
		textFieldEmailAddress.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(167, 155, 150, 20);
		
		lblUser = new JLabel("Email address:");
		lblUser.setBounds(75, 120, 85, 14);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(75, 158, 65, 14);
		
		btnLogIn = new JButton("Log in");
		btnLogIn.setBounds(167, 193, 70, 23);
		
		lblResetPassword = new JLabel("Reset password");
		lblResetPassword.setBounds(254, 197, 95, 14);
		setLayout(null);
		add(lblLogo);
		add(lblUser);
		add(lblPassword);
		add(btnLogIn);
		add(lblResetPassword);
		add(passwordField);
		add(textFieldEmailAddress);

		//For entering new password
		pass1 = new  JPasswordField(16);
		pass2 = new  JPasswordField(16);
		
		passChange = new JPanel();
		passChange.add(new JLabel("Enter new password:"));
		passChange.add(pass1);
		passChange.add(Box.createVerticalStrut(15)); // a spacer
		passChange.add(new JLabel("Reenter new password"));
		passChange.add(pass2);
	}
	
	
	public void setEventHandlers(){
		
		//Login button:
		btnLogIn.addActionListener(e -> {
			String userID = textFieldEmailAddress.getText();
			char[] pass = passwordField.getPassword();
			if (Validator.validateLoginInput(userID, pass)){
				User user = controller.login(userID, pass);
				if (Validator.validateUser(user, pass)){
					showRoot();
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
		
		
	}
	

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLogin() {

	}

	@Override
	public void showRoot() {
		view.setFullScreen();
		view.setCurrentWindow(ROOT);
	}

}
