package server.model;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.postgresql.Driver;

import server.remote_business_enitities.*;
import server.remote_business_enitities.RCalendar;
import server.remote_business_enitities.RChat;
import server.remote_business_enitities.RMember;
import server.remote_business_enitities.RMemo;
import server.remote_business_enitities.RProject;
import shared.User;
import shared.remote_business_interfaces.RemoteMemberInterface;
import shared.remote_business_interfaces.RemoteMemoInterface;
import shared.remote_business_interfaces.RemoteProjectInterface;
import shared.remote_business_interfaces.RemoteProjectsInterface;
import utility.Cryptography;

/**
 * Purpose of the {@link ProjectDAO} class is to work directly with the database.
 * It creates the connection to the database from which {@link ProjectDAO} class can read, update, add and delete the data using methods that are implemented below. 
 */
public class ProjectDAO {
    private static ProjectDAO instance;
    private final Connection connection;

    /**
     * Returns the object that contains the connection to the server.
     * @return {@link Connection} connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Returns an instance of {@link ProjectDAO} class and creates a new one if it isn't created already. 
     * @return {@link ProjectDAO} instance object.
     */
    public static synchronized ProjectDAO getInstance() {
        if (instance == null) {
            try {
                instance = new ProjectDAO();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * 
     * @throws SQLException if the address to the date was written incorrectly. 
     */
    private ProjectDAO() throws SQLException {
        DriverManager.registerDriver(new Driver());
        connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "6640");
    }

    /**
     * Reads projects from the database.
     * Creates {@link RemoteProjectInterface} projects ArrayList object.
     * Creates {@link RemoteProjectsInterface} remoteProjects object.
     * Creates {@link RProject} remoteProject object for each project that was found.
     * Sets name, chat, calendar and members for each {@link RProject} remoteProject object created.
     * Adds {@link RProject} remoteProject objects into {@link RemoteProjectInterface} projects ArrayList object.
     * Adds members into {@link RemoteProjectsInterface} remoteProjects object calling the {@link setMembers()} method.
     * Adds {@link RemoteProjectInterface} projects ArrayList object into {@link RemoteProjectsInterface} remoteProjects object.
     * @return the {@link RemoteProjectsInterface} remoteProjects object that contains all projects read from the database.
     */
    public RemoteProjectsInterface readAllProjects() {
        ArrayList<RemoteProjectInterface> projects = new ArrayList<>();
        try {
            RemoteProjectsInterface remoteProjects = new RProjects();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM project");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                RProject remoteProject = new RProject(result.getString("project_name"));
                remoteProject.setChat(readChat(result.getString("project_name")));
                remoteProject.setCalendar(readCalendar(result.getString("project_name")));
                remoteProject.setMembers(readParticipants(result.getString("project_name")));
                projects.add(remoteProject);
            }
            remoteProjects.setMembers(readMembers());
            remoteProjects.setProjects(projects);
            return remoteProjects;

        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Reads users from the database.
     * Creates {@link User} users ArrayList object.
     * Creates {@link User} user objects for each user that was found with email, name and phone.
     * Encrypts the password using {@link encryptPass()} method and sets the password using {@link getKey()} for each {@link User} user object created.
     * Adds all {@link User} user objects into {@link User} users ArrayList object.
     * @return the {@link User} users ArrayList object that contains all users read from the database.
     */
    public ArrayList<User> readAllUsers() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                User user = new User(result.getString("user_email"), result.getString("user_name"),
                        result.getString("user_phone"), null);
                user.setPass(Cryptography.encryptPass(result.getString("user_password").toCharArray(),
                        Cryptography.getKey()));
                users.add(user);
            }
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return users;
    }

    /**
     * Reads project names from the database in which user participates.
     * Creates {@link String} listOfprojectsNames ArrayList object.
     * Adds into {@link String} listOfprojectsNames ArrayList object projects names that were found.
     * @param email by which the method is able to read the specific projects. 
     * @return the {@link String} listOfprojectsNames ArrayList object that contains project names read from the database. 
     */
    public ArrayList<String> readProjectNamesForUser(String email){
        ArrayList<String> listOfProjectNames = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT project_name FROM registration WHERE user_email = ?");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                listOfProjectNames.add(result.getString("project_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfProjectNames;
    }

    /**
     * Reads projects from the database in which user participates.
     * Creates {@link RemoteProjectInterface} projects ArrayList object.
     * Creates {@link RemoteProjectsInterface} usersRemoteProjects object.
     * Creates {@link RProject} remoteProject object for each project that was found. 
     * Sets name, chat, calendar and members for each {@link RProject} remoteProject object created.
     * Adds {@link RProject} remoteProject objects into {@link RemoteProjectInterface} projects ArrayList object.
     * Sets {@link RemoteProjectInterface} projects ArrayList object to {@link RemoteProjectsInterface} usersRemoteProjects object.
     * @param email by which the method is able to read the specific projects. 
     * @return the {@link RemoteProjectsInterface} usersRemoteProjects object that contains projects read from the database. 
     */
    public RemoteProjectsInterface readAllProjectsForUser(String email) {

        ArrayList<RemoteProjectInterface> projects = new ArrayList<RemoteProjectInterface>();
        try {
            RemoteProjectsInterface usersRemoteProjects = new RProjects();
            PreparedStatement statement = connection.prepareStatement("SELECT project_name FROM registration WHERE user_email = ?");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                RProject remoteProject = new RProject(result.getString("project_name"));
                remoteProject.setChat(readChat(result.getString("project_name")));
                remoteProject.setCalendar(readCalendar(result.getString("project_name")));
                remoteProject.setMembers(readParticipants(result.getString("project_name")));
                projects.add(remoteProject);
            }
            //usersRemoteProjects.setMembers(readMembers());
            usersRemoteProjects.setProjects(projects);
            return usersRemoteProjects;
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads project from the database.
     * Creates {@link RProject} project object.
     * Sets name, chat, calendar and members for {@link RProject} project object. 
     * @param projectName by which the method is able to read the specific project.
     * @return the {@link RProject} project object that contains project read from the database. 
     */
    public RProject readProject(String projectName) {
        try {
            RProject project = new RProject(projectName);

            PreparedStatement statement = connection.prepareStatement("SELECT project_name FROM project WHERE project_name = ?");
            statement.setString(1, projectName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                project.setChat(readChat(result.getString("project_name")));
                project.setCalendar(readCalendar(result.getString("project_name")));
                project.setMembers(readParticipants(result.getString("project_name")));
            }
            return project;
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads chat from the database.
     * Creates {@link RChat} chat object.
     * Sets messages for {@link RChat} chat object.
     * @param projectName by which the method is able to read the specific chat.
     * @return the {@link RChat} chat object that contains messages read from the database.
     */
    public RChat readChat(String projectName) {
        try {
            RChat chat = new RChat();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM message WHERE project_name = ?");
            statement.setString(1, projectName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                chat.addMessage(result.getString("message_body"));
            }
            return chat;
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads calendar from the database.
     * Creates {@link RCalendar} calendar object.
     * Creates {@link RMemo} memo object.
     * Sets memos date and description for the {@link RemoteMemoInterface} memo object.
     * Adds {@link RemoteMemoInterface} memo objects into {@link RCalendar} calendar object.
     * @param projectName by which the method is able to read the specific calendar.
     * @return the {@link RCalendar} calendar object that contains memos with dates and descriptions read from the database.
     */
    public RCalendar readCalendar(String projectName) {
        try {
            RCalendar calendar = new RCalendar();

            PreparedStatement statement = connection.prepareStatement("SELECT memo_date, memo_description FROM memo WHERE project_name = ?");
            statement.setString(1, projectName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                RemoteMemoInterface memo = new RMemo(result.getDate("memo_date"), result.getString("memo_description"));
                calendar.addMemo(memo);
            }
            return calendar;
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads members from the database
     * Creates {@link RemoteMemberInterface} members ArrayList object.
     * Creates {@link RMember} member object for each member that was found with email and name.
     * Adds {@link RMember} member objects into {@link RemoteMemberInterface} members ArrayList object.
     * @return the {@link RemoteMemberInterface} members ArrayList object that contains members all members read from the database.
     */
    public ArrayList<RemoteMemberInterface> readMembers() {
        ArrayList<RemoteMemberInterface> members = new ArrayList<RemoteMemberInterface>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM member");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                RemoteMemberInterface member = new RMember(result.getString("member_email"), result.getString("member_name"));
                members.add(member);
            }
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return members;
    }

    /**
     * Read participants from the database.
     * Creates {@link RemoteMemberInterface} members ArrayList object.
     * Creates {@link RMember} member objects foe each participants that were found with email and name.
     * Adds {@link RMember} member objects into {@link RemoteMemberInterface} members ArrayList object
     * @param projectName by which the method is able to read the specific participants.
     * @return the {@link RemoteMemberInterface} members ArrayList object that contains all participants read from the database.
     */
    public ArrayList<RemoteMemberInterface> readParticipants(String projectName) {
        ArrayList<RemoteMemberInterface> members = new ArrayList<RemoteMemberInterface>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT member_name, member_email FROM member WHERE member_email IN(SELECT member_email FROM participation WHERE project_name = ?)");
            statement.setString(1, projectName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                RemoteMemberInterface member = new RMember(result.getString("member_email"), result.getString("member_name"));
                members.add(member);
            }
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return members;
    }

    /**
     * Adds project into the database.
     * @param projectName that is inserted into the database.
     */
    public void addProject(String projectName) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO project(project_name) VALUES (?)");
            statement.setString(1, projectName);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Adds message into the database.
     * @param projectName by which the method is able to detect in which project should the message be added.
     * @param message that is inserted into the database.
     */
    public void addMessage(String projectName, String message) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO message(project_name, message_body) VALUES (?, ?)");
            statement.setString(1, projectName);
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Adds memo into the database.
     * Gets date using {@link getDate()} method and description using {@link getDescription()} method from the @param memo and adds them into the database.
     * @param projectName by which the method is able to detect in which project should the memo be added.
     * @param memo from which the method takes date and description.
     */
    public void addMemo(String projectName, RMemo memo) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO memo(project_name, memo_date, memo_description) VALUES(?, ?, ?)");
            statement.setString(1, projectName);
            statement.setDate(2, new java.sql.Date(memo.getDate().getTime()));
            statement.setString(3, memo.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds member into the database.
     * Gets email using {@link getEmail()} method and name using {@link getName()} method from the @param member and adds them into the database.
     * @param member from which the method takes email and name.
     */
    public void addMember(RMember member) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO member(member_email, member_name) VALUES(?, ?)");
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO user_(user_email, user_name) VALUES(?, ?)");
            statement.setString(1, member.getEmail());
            statement.setString(2, member.getName());
            statement1.setString(1, member.getEmail());
            statement1.setString(2, member.getName());
            statement.executeUpdate();
            statement1.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds participation into the database.
     * Gets email using {@link getEmail()} method and adds them into the database.
     * @param projectName by which the method is able to detect in which project should the participation be added.
     * @param member from which the method takes email.
     */
    public void addParticipation(String projectName, RemoteMemberInterface member) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO participation(project_name, member_email) VALUES(?, ?)");
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO registration(project_name, user_email) VALUES(?, ?)");
            statement.setString(1, projectName);
            statement.setString(2, member.getEmail());
            statement1.setString(1, projectName);
            statement1.setString(2, member.getEmail());
            statement.executeUpdate();
            statement1.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates project in the database.
     * @param projectName by which the method is able to detect what project should be updated.
     * @param newProjectName that is inserted as an updated.
     */
    public void updateProject(String projectName, String newProjectName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE project SET project_name = ? WHERE project_name = ?");
            statement.setString(1, newProjectName);
            statement.setString(2, projectName);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Updated memo in the database.
     * Gets date using {@link getDate()} method and description using {@link getDescription()} method from the @param memo and updates the memo in the database.
     * @param projectName by which the method is able to detect in which project the memo is located.
     * @param memo from which the method takes date and description.
     */
    public void updateMemo(String projectName, RMemo memo) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ?, memo_description = ?  WHERE memo_date = ? AND project_name = ?");
            statement.setDate(1, new java.sql.Date(memo.getDate().getTime()));
            statement.setString(2, memo.getDescription());
            statement.setDate(3, new java.sql.Date(memo.getDate().getTime()));
            statement.setString(4, projectName);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates memo name in the database.
     * Gets the date from @param memo using {@link getDate()} method to detect the specific memo.
     * Changes the old name with @param newMemoName.
     * @param projectName by which the method is able to detect in which project the memo is located.
     * @param memo from which the method takes the date and is able to detect the specific memo in the database.
     * @param newMemoName that is inserted as a new name in the database.
     */
    public void updateMemoName(String projectName, RMemo memo, String newMemoName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_description = ? WHERE memo_date = ? AND project_name = ?");
            statement.setString(1, newMemoName);
            statement.setDate(2, new java.sql.Date(memo.getDate().getTime()));
            statement.setString(3, projectName);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates memo date in the database.
     * Gets the date from @param memo using {@link getDate()} method to detect the specific memo.
     * Changes the old date with @param newMemoDate.
     * @param projectName by which the method is able to detect in which project the memo is located.
     * @param memo from which the method takes the date and is able to detect the specific memo in the database.
     * @param newMemoDate from which the date is inserted as a new one in the database.
     */
    public void updateMemoDate(String projectName, RMemo memo, Date newMemoDate) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ? WHERE memo_date = ? AND project_name = ?");
            statement.setDate(1, new java.sql.Date(newMemoDate.getTime()));
            statement.setDate(2, new java.sql.Date(memo.getDate().getTime()));
            statement.setString(3, projectName);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes project from the database.
     * @param project by which the method is able to detect the specific project in the database.
     * @throws RemoteException if the RMI connection failed.
     */
    public void deleteProject(RemoteProjectInterface project) throws RemoteException {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM participation WHERE project_name = ?");
            PreparedStatement statement1 = connection.prepareStatement("DELETE FROM registration WHERE project_name = ?");
            PreparedStatement statement2 = connection.prepareStatement("DELETE FROM message WHERE project_name = ?");
            PreparedStatement statement3 = connection.prepareStatement("DELETE FROM memo WHERE project_name = ?");
            PreparedStatement statement4 = connection.prepareStatement("DELETE FROM project WHERE project_name = ?");
            statement.setString(1, project.getName());
            statement1.setString(1, project.getName());
            statement2.setString(1, project.getName());
            statement3.setString(1, project.getName());
            statement4.setString(1, project.getName());
            statement.executeUpdate();
            statement1.executeUpdate();
            statement2.executeUpdate();
            statement3.executeUpdate();
            statement4.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Deletes memo from the database.
     * @param projectName by which the method is able to detect in which project the memo is situated.
     * @param memo from which the method takes date to indicate the specific memo.
     */
    public void deleteMemo(String projectName, RemoteMemoInterface memo) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM memo WHERE memo_date = ? AND project_name = ?");
            statement.setDate(1, new java.sql.Date(memo.getDate().getTime()));
            statement.setString(2, projectName);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes participant from the database.
     * @param projectName by which the method is able to detect in which the project the participant is part of.
     * @param member from which the method takes email to indicate the specific participant.
     */
    public void deleteParticipant(String projectName, RemoteMemberInterface member) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM participation WHERE member_email = ? AND project_name = ?");
            PreparedStatement statement1 = connection.prepareStatement("DELETE FROM registration WHERE user_email = ? AND project_name = ?");
            statement.setString(1, member.getEmail());
            statement.setString(2, projectName);
            statement1.setString(1, member.getEmail());
            statement1.setString(2, projectName);
            statement.executeUpdate();
            statement1.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
