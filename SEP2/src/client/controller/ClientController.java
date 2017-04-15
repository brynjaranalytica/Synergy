package client.controller;


import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JOptionPane;

import client.model.ClientModel;
import client.view.Login;
import client.view.View;
import shared.ClientInterface;
import shared.ServerInterface;
import shared.User;
import utility.Log;
import utility.PINcode;
import utility.PropertiesWriter;

public class ClientController implements ClientInterface, Serializable{
	
	private static final long serialVersionUID = 1L;
	private static Properties properties;
	private static final String PROPFILE = "client.properties";
	private static final String IP = "IP";
	private static final String SERVERNAME = "Servername";
	private static final File LOCK_FILE = new File("synergy_client.lock");
	private ServerInterface serverController;
	private static Log log;
	private ClientModel clientModel;
	private PINcode pinCode;
	
	
	public static void main(String[] args) {
		
		if (! LOCK_FILE.exists()){ //Preventing application from being launched if already running
			createLockFile();
			EventQueue.invokeLater(() -> { 	new View(); });
		} else {
			JOptionPane.showMessageDialog(null, "Synergy is already running !");
		}
	}
	
	private static class Wrapper{ //Instance placed in inner class
		static ClientController instance = new ClientController();	//Created in memory when called	
	}

	
	public static ClientController getInstance(){
		return Wrapper.instance; //Instantiates the instance when called
	}
	
	private ClientController(){
//		new Login(this);
		clientModel = new ClientModel();
		log = Log.getInstance();
		pinCode = PINcode.getInstance();
		loadProperties();
		if (System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}
		try {
			UnicastRemoteObject.exportObject(this, 0);
			serverController = (ServerInterface) Naming.lookup("rmi://"+properties.getProperty(IP)+"/"+properties.getProperty(SERVERNAME));

		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println(e.getMessage());
			String result = JOptionPane.showInputDialog(null, "Synergy server not found on network.\n\nPlease check if server is started or try to re-enter the host address and start client app again.\nCurrent set host adress: "+properties.getProperty(IP), "Reenter server ip-address", 1);
			
			if (result ==null){
				System.exit(0);
			} else {				
				Hashtable<String, String> newProperties = new Hashtable<>();
				newProperties.put(IP, result);
				newProperties.put(SERVERNAME, ClientController.properties.getProperty(SERVERNAME));		
				try {
					PropertiesWriter.writeProperties(newProperties, PROPFILE, "Synergy Client Properties File");
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
	}
	
	public int getPIN(String phoneNumber){
		return pinCode.requestPin(phoneNumber);
	}
	
	public boolean savePass(String userID, char[] pass){
		try {
			return serverController.savePass(userID, pass);
		} catch (RemoteException e) {

			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public String getIP() {
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String validateID(String userID){
		try {
			return serverController.validateId(userID);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	public User login(String userID, char[] password) {
		User user = null;
		try {
			user = serverController.login(userID, password);
			if (user!=null) clientModel.setUser(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public void displayProjects(){
		//The user is logged in and this method will display all projects the user is in
		
		JOptionPane.showMessageDialog(null, "The user is now logged in and we need to display the projects he is in", "Logged in succesfully", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void createLockFile() {
	    try {
			LOCK_FILE.createNewFile();
		} catch (IOException e1) {
			System.out.println("Unable to create lock file");
		}    
	    
	    Thread shutdown = new Thread(() ->{
	    	try {
				LOCK_FILE.delete();
			} catch (Exception e) {
				System.out.println("Unable to delete lock file - please delete manually: "+e.getMessage());
			}
	    });
	    
	    Runtime.getRuntime().addShutdownHook(shutdown);
	    shutdown.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				shutdown.start();
				System.exit(-1);
			}
		});
	
	}
	
	private static void loadProperties(){
		try (InputStream in = new FileInputStream(PROPFILE)){
			properties = new Properties();
			properties.load(in);
		} catch (FileNotFoundException e) {
			log.logWarning("Properties file not found!", e.getMessage());
		} catch (IOException e) {
			log.logWarning("Error reading properties file", e.getMessage());
		}
	}




}
