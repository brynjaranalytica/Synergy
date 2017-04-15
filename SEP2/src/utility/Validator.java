package utility;

import java.util.Arrays;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JOptionPane;

import shared.User;

public class Validator {
	
	//Helper method for validating email address pattern
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
	
	//Helper method for validating format of entered input from login screen
	public static boolean validateLoginInput(String userID, char[] pass){
		
		if (userID.isEmpty()||userID==null) {
			JOptionPane.showMessageDialog(null,"Email address is required - please try again", "Failed to log in", JOptionPane.OK_OPTION);
			return false;
		} else if (!Validator.isValidEmailAddress(userID)) {
			JOptionPane.showMessageDialog(null,"Valid email address is required - please try again", "Failed to log in", JOptionPane.OK_OPTION);
			return false;
		} else if (pass.length<6 || pass == null) {
			JOptionPane.showMessageDialog(null,"Password of minimum 6 characters is required.\nPlease try again", "Failed to log in", JOptionPane.OK_OPTION);
			return false;
		} else {
			return true;
		}
	}
	
	//Helper method for validating user id and password
	public static boolean validateUser(User user, char[] pass){
		if (user != null){
			char[] passStored = user.getPass();
			if (passStored!=null && Arrays.equals(pass, passStored)){
				return true;			
			} else {
				JOptionPane.showMessageDialog(null,"Log in failed.\nPlease check your email address and password and try again", "Failed to log in", JOptionPane.OK_OPTION);
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null,"Log in failed.\nPlease check your email address and password and try again", "Failed to log in", JOptionPane.OK_OPTION);
			return false;
		}
	}
}
