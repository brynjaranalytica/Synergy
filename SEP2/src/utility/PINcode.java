package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * 	Implements the Singleton pattern
 */
public class PINcode {
	
	private static Properties properties;
	private static final String PROPFILE = "pin.properties";
	private static final String MESSAGE = "Message";
	private static final String SENDER = "Sender";
	
	private static class Wrapper{ //Instance placed in inner class
		static PINcode instance = new PINcode();	//Created in memory when called	
	}
	
	private PINcode(){
		
	}
	
	public static PINcode getInstance() { 	
		Log log = Log.getInstance();
		try (InputStream in = new FileInputStream(PROPFILE)){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			log.logSevere("No properties file found!", e.getMessage());
		} catch (IOException e) {
			log.logSevere("Error reading properties file!", e.getMessage());
		}
		return Wrapper.instance; //Instantiates the instance when called
	}
	
	private static int generateNewPin(){
		return (int)Math.floor((Math.random()*9000)+1000);
	}
	
	public synchronized  int requestPin(String phoneNumber){	
		int pin =generateNewPin();
		//Send new pin as SMS to user
		SMSconnector.sendSMS(phoneNumber, properties.getProperty(MESSAGE), pin,  properties.getProperty(SENDER));
		
		//Return pin to verify matching pin entered by user
		return pin;
	}
	
}
