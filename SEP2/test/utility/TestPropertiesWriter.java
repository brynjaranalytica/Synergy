package utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
/*
 * This class can be used if you want to create - or change - a properties file 
 */

public class TestPropertiesWriter {
	
	private static void writeProperties(Hashtable<String, String> properties, String filename, String description) throws IOException{
		Properties prop = new Properties();
	
		for (Map.Entry<String, String> entry : properties.entrySet()){
			prop.setProperty(entry.getKey(), entry.getValue());
		}
		
		try (OutputStream out = new FileOutputStream(filename)){
			prop.store(out, description);
		}
	}
	
	public static void main(String[] args) throws IOException {
//		//Name the file:
//		String fileName = "pin.properties";
//		//Add a description:
//		String description = "Synergy PIN Properties File";
//		Hashtable<String, String> properties = new Hashtable<>();
//		//Add all the properties you want to write to file like this:
//		properties.put("Message", "Here is your PIN code for resetting your password: ");
//		properties.put("Sender", "Synergy");
//		writeProperties(properties, fileName, description);

//		//Name the file:
//		String fileName = "smsconnector.properties";
//		//Add a description:
//		String description = "Synergy SMS Gateway Properties File";
//		Hashtable<String, String> properties = new Hashtable<>();
//		//Add all the properties you want to write to file like this:
//		properties.put("SmsUrl", "https://gatewayapi.com/rest/mtsms");
//		properties.put("Token", "WNCxV0NqQi6dqUTZHnsMvIoNLzRHN_P_pG6kDF3xfpFZgkAdz6oeKIeWh0sEE7n8");		
//		writeProperties(properties, fileName, description);
		
		//Name the file:
		String fileName = "client.properties";
		//Add a description:
		String description = "Synergy Client Properties File";
		Hashtable<String, String> properties = new Hashtable<>();
		//Add all the properties you want to write to file like this:
		properties.put("IP", "localhost");
		properties.put("Servername", "SynergyServer");		
		writeProperties(properties, fileName, description);
	}

}
