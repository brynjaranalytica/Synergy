package server.controller;


import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Properties;
import javax.swing.JOptionPane;

import server.model.ServerModel;
import server.view.GUI;
import shared.ServerInterface;
import shared.User;
import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.Cryptography;
import utility.Log;

public class ServerController implements ServerInterface{
	
	@SuppressWarnings("unused")
	private GUI gui;
	private ServerModel serverModel;
	private static Properties properties;
	private static final String PROPFILE = "server.properties";
	private static final String PORT = "Port";
	private static final String SERVERNAME = "Servername";
	private static final File LOCK_FILE = new File("synergy_server.lock");
	private static Log log;
	
	public static void main(String[] args) {
		
		
		if (! LOCK_FILE.exists()){ //Preventing application from being launched if already running
			createLockFile();
			EventQueue.invokeLater(() -> {
				try {
					new ServerController();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			});
		} else {
			JOptionPane.showMessageDialog(null, "Synergy server is already running !");
		}
		 
	}
	
	public ServerController() throws RemoteException {
		System.setProperty("java.rmi.server.hostname","localhost");
		log = Log.getInstance();
		gui = new GUI(this);
		serverModel = new ServerModel();
		loadProperties();
		try {
			UnicastRemoteObject.exportObject(this, 0);
			@SuppressWarnings("unused")
			Registry registry = LocateRegistry.createRegistry(Integer.parseInt(properties.getProperty(PORT)));
			Naming.rebind(properties.getProperty(SERVERNAME), this);
		} catch (RemoteException | MalformedURLException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	@Override
	public boolean savePass(String userID, char[] pass) throws RemoteException {
		return serverModel.savePass(userID, pass);
	}

	@Override
	public RemoteProjectsInterface getRemoteProjectsForUser(User user) throws RemoteException {
		return serverModel.getRemoteProjectsForUser(user.getiD());
	}

	@Override
	public String validateId(String userID) throws RemoteException {
		return serverModel.checkID(userID);
	}

	@Override
	public User login(String userID, char[] passWord) throws RemoteException {
		User user = serverModel.retrieveUser(userID);
		if (user == null) return null;
		char[] passEncrypted = user.getPass();
		if (passEncrypted == null) return null;
		char[] passDecrypted = Cryptography.decryptPass(passEncrypted, Cryptography.getKey());
		if (userID.equalsIgnoreCase(user.getiD()) && Arrays.equals(passWord, passDecrypted)){
			user.setPass(passDecrypted);
			return user;
		} else {
			return null;
		}
	}
	
	//Allowing only one instance running
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
				System.out.println("Unable to delete lock file - please delete manually");
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
	
	//Reading the server properties instead of hard coding them
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

	public static String getIP() {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			return ip;
		} catch (UnknownHostException e) {
			return "(Unable to retrieve IP address local host)";
		}

	}



}
