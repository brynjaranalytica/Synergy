package client.controller;


import client.model.ClientModel;
import client.view.Root;
import client.view.View;
import server.remote_business_enitities.RMemo;
import shared.*;
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

public class ClientController implements ClientInterface, Serializable, RemoteObserver<UpdateMessage> {

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
        switch (message.getHeader()) {
            case MessageHeaders.UPDATE:
                clientModel.setProject((Project) message.getBusinessEntity());
                view.loadData();
                break;
            case MessageHeaders.CREATE:
                clientModel.addProject((Project) message.getBusinessEntity());
                view.loadData();
                break;
            case MessageHeaders.DELETE:
                clientModel.removeProject(((Project) message.getBusinessEntity()).getName());
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

    @Override
    public String getIP() throws RemoteException {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String validateID(String userID) {
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

    public static Project getProjectFromServer(String name) {
        try {
            return new Project(remoteProjects.getProject(name));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;

    }

    public ProjectInterface getProjectFromModel(String projectName) {
        return clientModel.getProject(projectName);
    }

    public ProjectInterface getProjectFromModel(int index) {
        return clientModel.getProject(index);
    }

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
            remoteProjects.addProject(project);
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
