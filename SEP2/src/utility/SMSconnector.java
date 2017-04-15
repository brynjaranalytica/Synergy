package utility;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

public class SMSconnector {
	
	private static Properties properties;
	private static final String PROPFILE = "smsconnector.properties";
	private static URL url;
	private static final String SMSURL = "SmsUrl";
	private static final String TOKEN= "Token";
	private static HttpsURLConnection connection;
	private static DataOutputStream outputStream;
	
	public synchronized static void sendSMS(String phoneNumber, String message, int pin, String sender){
		Log log = Log.getInstance();
		try (InputStream in = new FileInputStream(PROPFILE)){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			log.logWarning("Properties file not found!", e.getMessage());
		} catch (IOException e) {
			log.logWarning("Error reading properties file", e.getMessage());
		}
		try {
			url = new URL(properties.getProperty(SMSURL));
		} catch (MalformedURLException e) {
			log.logWarning("Error parsing url", e.getMessage());
		}
		try {
			connection = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			log.logWarning("Error creating url connection", e.getMessage());
		}
		connection.setDoOutput(true);
		try {
			outputStream = new DataOutputStream(connection.getOutputStream());
		} catch (IOException e) {
			log.logWarning("Error creating output stream", e.getMessage());
		}
		try {
			outputStream.writeBytes(
					"token="+properties.getProperty(TOKEN) 
					+ "&sender=" + URLEncoder.encode(sender, "UTF-8")
					+ "&message=" + URLEncoder.encode(message+pin, "UTF-8")
					+ "&recipients.0.msisdn="+phoneNumber
					);
		} catch (IOException e) {
			log.logSevere("Error sending sms stream", e.getMessage());
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			log.logInfo("Error closing output stream connection", e.getMessage());
		}
		
		int responseCode = -1;
		try {
			responseCode = connection.getResponseCode();
		} catch (IOException e) {
			log.logWarning("No sms response received", e.getMessage());
		}
		if (responseCode!=200){
			log.logWarning("SMS send request failed!", Integer.toString(responseCode));
		}
	}
}
