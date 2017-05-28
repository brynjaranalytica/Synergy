package client.controller;


import client.model.ClientModel;
import client.view.windows.Root;
import client.view.windows.View;
import shared.MessageHeaders;
import shared.ServerInterface;
import shared.UpdateMessage;
import shared.User;
import shared.business_entities.*;
import shared.remote_business_interfaces.RemoteMemberInterface;
import shared.remote_business_interfaces.RemoteProjectInterface;
import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.Log;
import utility.PINcode;
import utility.PropertiesWriter;
import utility.observer.RemoteObserver;
import utility.observer.RemoteSubject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

public class ClientController implements RemoteObserver<UpdateMessage> {

    private static final long serialVersionUID = 1L;
    private static Properties properties;
    private static final String PROPFILE = "client.properties";
    private static final String IP = "IP";
    private static final String SERVERNAME = "Servername";
    private static final File LOCK_FILE = new File("synergy_client.lock");
    private static Log log;
    private PINcode pinCode;

    private ServerInterface serverController;
    private static View view;
    private static RemoteProjectsInterface remoteProjects;
    private ClientModel clientModel;


    public static void main(String[] args) {

        if (!LOCK_FILE.exists()) { //Preventing application from being launched if already running
            createLockFile();
            EventQueue.invokeLater(() -> {
                view = new View();
            });
        } else {
            JOptionPane.showMessageDialog(null, "Synergy is already running !");
        }
    }

    @Override
    public void update(RemoteSubject<UpdateMessage> remoteSubject, UpdateMessage message) throws RemoteException {
        Project project = (Project) message.getBusinessEntity();
        switch (message.getHeader()) {
            case MessageHeaders.UPDATE:
                clientModel.setProject(project);
                view.loadData();
                break;
            case MessageHeaders.CREATE:
                clientModel.addProject(project);
                view.loadData();
                break;
            case MessageHeaders.DELETE:
                String projectName = project.getName();
                Root.currentProjectName = projectName.equals(Root.currentProjectName)?
                                                null :
                                                Root.currentProjectName;
                clientModel.removeProject(projectName);
                view.loadData();
                break;
        }
    }

    private static class Wrapper { //Instance placed in inner class
        static ClientController instance = null;

        static {
            try {
                instance = new ClientController();    //Created in memory when called
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static ClientController getInstance() {
        return Wrapper.instance; //Instantiates the instance when called
    }

    private ClientController() throws RemoteException {
        clientModel = new ClientModel();
        log = Log.getInstance();
        pinCode = PINcode.getInstance();
        loadProperties();
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            UnicastRemoteObject.exportObject(this, 0);
            serverController = (ServerInterface) Naming.lookup("rmi://" + properties.getProperty(IP) + "/" + properties.getProperty(SERVERNAME));

        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            String result = JOptionPane.showInputDialog(null, "Synergy server not found on network.\n\nPlease check if server is started or try to re-enter the host address and start client app again.\nCurrent set host adress: " + properties.getProperty(IP), "Reenter server ip-address", 1);

            if (result == null) {
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

    public int getPIN(String phoneNumber) {
        return pinCode.requestPin(phoneNumber);
    }

    public boolean savePass(String userID, char[] pass) {
        try {
            return serverController.savePass(userID, pass);
        } catch (RemoteException e) {

            e.printStackTrace();
        }
        return false;
    }

   /* @Override
    public String getIP() throws RemoteException {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * Security method for checking credentials entered by client. More precise, it is responsible for initial checking,
     * whether the entered email represents a valid email address.
     * @param userID
     * @return
     */
    public String validateID(String userID) {
        try {
            return serverController.validateId(userID);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method responsible for login operation. First client requests a User object from the server. The user object will
     * represent the account, whose credentials where entered at Login window. If login was successful, client then requests
     * a set of projects from server. This set of projects will contain only the projects, which user is part of. It
     * is important to notice that this set of projects is a remote object.
     * Client then registers itself as an remote observer in each project received. The last step is to populate the client model
     * with initial information: company name, project names (initProxyProjects() method). Project names are needed to identify the proxy projects.
     * @param userID
     * @param password
     * @return
     */
    public User login(String userID, char[] password) {

        User user = null;
        try {
            user = serverController.login(userID, password, this);
            if (user != null) {
                clientModel.setUser(user);
                remoteProjects = serverController.getRemoteProjectsForUser(user);
                //remoteProjects.addObserver(this);
                registerAsObserverForProjects();
                clientModel.setOrganizationName(remoteProjects.getName());
                clientModel.initProxyProjects(getProjectNamesFromServer());
                clientModel.getProjects().setMembers(getOrganizationMembersFromServer());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Iterates through the list of projects and registers itself as a remote observer in each of them.
     * Each project object is remote.
     */
    private void registerAsObserverForProjects(){
        try {
            ArrayList<RemoteProjectInterface> remoteProjects = ClientController.remoteProjects.getRemoteProjects();
            for(RemoteProjectInterface remoteProject: remoteProjects){
                remoteProject.addObserver(this);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates lock file for client application. This guarantees that only the application can be started only once on the
     * same computer.
     */
    private static void createLockFile() {
        try {
            LOCK_FILE.createNewFile();
        } catch (IOException e1) {
            System.out.println("Unable to create lock file");
        }

        Thread shutdown = new Thread(() -> {
            try {
                LOCK_FILE.delete();
            } catch (Exception e) {
                System.out.println("Unable to delete lock file - please delete manually: " + e.getMessage());
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


    /**
     * Loads the information from .properties file using java.IO. Those files contain constant information regarding
     * client-server communication: port numbers, IP addresses.
     */
    private static void loadProperties() {
        try (InputStream in = new FileInputStream(PROPFILE)) {
            properties = new Properties();
            properties.load(in);
        } catch (FileNotFoundException e) {
            log.logWarning("Properties file not found!", e.getMessage());
        } catch (IOException e) {
            log.logWarning("Error reading properties file", e.getMessage());
        }
    }


    /**
     * Gets the array list of project names from server model. Used to populate fields in GUI.
     * @return
     */
    public ArrayList<String> getProjectNames() {
        ArrayList<String> projectNames = new ArrayList<>();
        ArrayList<ProjectInterface> projects = clientModel.getProjects().getProjectList();

        for (ProjectInterface project : projects) {
            projectNames.add(project.getName());
        }


        return projectNames;
    }

    public ArrayList<String> getProjectNamesFromServer() {
        try {
            return remoteProjects.getProjectNames();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This method returns the Project object from the server. Communication is done through "remoteProjects" object,
     * which is, as name suggests, a remote object. First, client will receive the instance of RProject class, which is
     * a remote class. The remote project is then transformed into non-remote, local object using the constructor of
     * Project class (e.g. new Project(RProject remoteProject) ). This method is used by proxy project for loading the
     * "real" project.
     * @param name
     * @return
     */
    public static Project getProjectFromServer(String name) {
        try {
            return new Project(remoteProjects.getProject(name));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (NullPointerException ex){
            return null;
        }

        return null;

    }

    /**
     * Return instance of non-remote interface ProjectInterface. Usually, it is an instance of ProxyProject.
     * @param projectName
     * @return
     */
    public ProjectInterface getProjectFromModel(String projectName) {
        return clientModel.getProject(projectName);
    }

    public ProjectInterface getProjectFromModel(int index) {
        return clientModel.getProject(index);
    }

    /**
     * Sends a request to sever. Client requests to add new message to the chat of the specific project. Projects
     * are identified by the name.
     * @param projectName
     * @param message
     */
    public void sendChatMessage(String projectName, String message) {
        try {
            remoteProjects.getProject(projectName).addMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addMemo(String projectName, Date date, String description) {
        try {
            remoteProjects.getProject(projectName).addMemo(new Memo(date, description));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeMemo(String projectName, Date date) {
        try {
            remoteProjects.getProject(projectName).removeMemo(date);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addProject(ProjectInterface project) {
        if (clientModel.getProjects().getProjectNames().contains(project.getName())) {
            JOptionPane.showMessageDialog(null,"The project with such name already exists!");
            return;
        }

        try {
            remoteProjects.addProject(project, clientModel.getUser().getiD());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(String projectName) {
        try {
            remoteProjects.removeProject(projectName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Member> getOrganizationMembersFromServer() {
        try {
            ArrayList<RemoteMemberInterface> remoteMembers = remoteProjects.getMembers();
            ArrayList<Member> members = new ArrayList<>();
            for (RemoteMemberInterface remoteMember : remoteMembers) {
                members.add(new Member(remoteMember));
            }

            return members;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Projects getProjectsFromModel() {
        return clientModel.getProjects();
    }

    public boolean addMember(int index) {
        Member member = clientModel.getProjects().getMembers().get(index);
        if(clientModel.getProject(Root.currentProjectName).getMembers().contains(member))
            return false;
        try {
            remoteProjects.getProject(Root.currentProjectName).addMember(clientModel.getProjects().getMembers().get(index).getEmail());
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeMember(String email) {
        try {
            remoteProjects.getProject(Root.currentProjectName).removeMember(email);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeMember(int index) {
        try {
            remoteProjects.getProject(Root.currentProjectName).removeMember(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
