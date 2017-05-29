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
import shared.UpdateMessage;
import shared.User;
import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.Cryptography;
import utility.Log;
import utility.observer.RemoteObserver;

/**
 * This class is mainly responsible for initial start of the server: creating server model,
 * creating and saving remote stub in the registry. The client, when being started will first try to grab instance of this
 * class from a registry. This class is also responsible for handling next operations: login, resetting password.
 */
public class ServerController implements ServerInterface{
	
	@SuppressWarnings("unused")
	/**
	 * A simple GUI for a server side. No particular functionality implemented.
	 */
	private GUI gui;

	/**
	 * An instance of serverModel for handling login operation.
	 */
	private ServerModel serverModel;

	/**
	 * A properties file containing such information as remote stub name and other constant info.
	 */
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

	/**
	 * Starts the server by saving the remote stub of itself into registry using java RMI. Loads data from "properties"
	 * files, initiates the server model, which will then read users and projects data from the database.
	 * @throws RemoteException
	 */
	public ServerController() throws RemoteException {
		System.setProperty("java.rmi.server.hostname","localhost");
		log = Log.getInstance();
		gui = new GUI(this);
		serverModel = ServerModel.getInstance();
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

	/**
	 * Method used for resetting password for the specific user account.
	 * @param userID
	 * @param pass
	 * @return
	 * @throws RemoteException
	 */
	@Override
	public boolean savePass(String userID, char[] pass) throws RemoteException {
		return serverModel.savePass(userID, pass);
	}

	/**
	 * This method is used for handling client request of getting a set of projects, which a user (who client is
	 * representing) is part of.
	 * @param user
	 * @return
	 * @throws RemoteException
	 */
	@Override
	public RemoteProjectsInterface getRemoteProjectsForUser(User user) throws RemoteException {
		RemoteProjectsInterface usersRemoteProjects =  serverModel.getRemoteProjectsForUser(user.getiD());
		serverModel.addProjectSet(user.getiD(), usersRemoteProjects);
		return usersRemoteProjects;
	}


	@Override
	public String validateId(String userID) throws RemoteException {
		return serverModel.checkID(userID);
	}

	/**
	 * Method responsible for login operation. Method will first try to retrieve the user from the server model by its email
	 * (because each user is uniquely identified by its email). It will then validate the password. If successful, it will
	 * save the connection with the client (represented by instance of RemoteObserver class) for future usages.
	 * @param userID
	 * @param passWord
	 * @param client
	 * @return instance of User class if login was successful. Method will return null if login was unsuccessful.
	 * @throws RemoteException
	 */
	@Override
	public User login(String userID, char[] passWord, RemoteObserver<UpdateMessage> client) throws RemoteException {
		User user = serverModel.retrieveUser(userID);
		if (user == null)
			return null;
		char[] passEncrypted = user.getPass();
		if (passEncrypted == null)
			return null;
		char[] passDecrypted = Cryptography.decryptPass(passEncrypted, Cryptography.getKey());
		if (userID.equalsIgnoreCase(user.getiD()) && Arrays.equals(passWord, passDecrypted)){
			user.setPass(passDecrypted);
			serverModel.addConnection(userID, client);
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
