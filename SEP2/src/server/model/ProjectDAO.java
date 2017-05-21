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

public class ProjectDAO
{
   private static ProjectDAO instance;
   private final Connection connection;

   public Connection getConnection() {
      return connection;
   }

   public static synchronized ProjectDAO getInstance()  {
      if (instance == null) {
         try {
            instance = new ProjectDAO();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return instance;
   }
   
   private ProjectDAO() throws SQLException {
      DriverManager.registerDriver(new Driver());
      connection = DriverManager
              .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
   }
   
   public RemoteProjectsInterface readAllProjects() throws SQLException, RemoteException {
      RemoteProjectsInterface remoteProjects = new RProjects();
      ArrayList<RemoteProjectInterface> projects = new ArrayList<RemoteProjectInterface>();
      try {
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM project");
         ResultSet result = statement.executeQuery(); 
         while (result.next()) {
            RProject remoteProject = new RProject(result.getString("project_name"));
            remoteProject.setChat(readChat(result.getString("project_name")));
            remoteProject.setCalendar(readCalendar(result.getString("project_name")));
            remoteProject.setMembers(readParticipants(result.getString("project_name")));
            projects.add(remoteProject);
        }
         
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      }
      remoteProjects.setProjects(projects);
      return remoteProjects;
   }

   public ArrayList<User> readAllUsers() throws SQLException, RemoteException {
      ArrayList<User> users = new ArrayList<User>();
      try
      {
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_");
         ResultSet result = statement.executeQuery(); 
         while (result.next()) {
            User user = new User(result.getString("user_email"), result.getString("user_name"), result.getString("user_phone"), null);
            users.add(user);
        }
      }
      catch (SQLException e)
      {
         try
         {
            connection.close();
         }
         catch (SQLException e2)
         {
            e2.printStackTrace();
         }
      }
      return users;
   }
   
   public RemoteProjectsInterface readAllProjectsForUser(String email) {

      ArrayList<RemoteProjectInterface> projects = new ArrayList<RemoteProjectInterface>();
      try
      {
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
         usersRemoteProjects.setProjects(projects);
         return usersRemoteProjects;
      }
      catch (SQLException e)
      {
         try
         {
            connection.close();
         }
         catch (SQLException e2)
         {
            e2.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
      return null;
   }
   
   public RProject readProject(String projectName)  {
      try
      {
         RProject project = new RProject(projectName);

         PreparedStatement statement = connection.prepareStatement("SELECT project_name FROM project WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery(); 
         while (result.next())
         {
            project.setChat(readChat(result.getString("project_name")));
            project.setCalendar(readCalendar(result.getString("project_name")));
            project.setMembers(readParticipants(result.getString("project_name")));
         }
         return project;
      }
      catch (SQLException e)
      {
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
   
   public  RChat readChat(String projectName)  {
      try {
         RChat chat = new RChat();

         PreparedStatement statement = connection.prepareStatement("SELECT * FROM message WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery(); 
         while (result.next()) {
            chat.addMessage(result.getString("message_body"));
        }
         return chat;
      }
      catch (SQLException e)
      {
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
      }
      catch (SQLException e)
      {
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
   
   public ArrayList<RemoteMemberInterface> readMembers()  {
      ArrayList<RemoteMemberInterface> members = new ArrayList<RemoteMemberInterface>();
      try
      {
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM member");
         ResultSet result = statement.executeQuery();
         while (result.next()) {
            RemoteMemberInterface member = new RMember(result.getString("member_email"),  result.getString("member_name"));
            members.add(member);
        }
      }
      catch (SQLException e)
      {
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
   
   public ArrayList<RemoteMemberInterface> readParticipants(String projectName) {
      ArrayList<RemoteMemberInterface> members = new ArrayList<RemoteMemberInterface>();
      try
      {
         PreparedStatement statement = connection.prepareStatement("SELECT member_name, member_email FROM member WHERE member_email IN(SELECT member_email FROM participation WHERE project_name = ?)");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery();
         while (result.next()) {
            RemoteMemberInterface member = new RMember(result.getString("member_email"),  result.getString("member_name"));
            members.add(member);
        }
      }
      catch (SQLException e)
      {
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
   
   public void addProject(String projectName)  {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO project(project_name) VALUES (?)");
         statement.setString(1, projectName);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try
         {
            connection.close();
         }
         catch (SQLException e2)
         {
            e2.printStackTrace();
         }
      }
   }
   
   public void addMessage(String projectName, String message)  {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO message(project_name, message_body) VALUES (?, ?)");
         statement.setString(1, projectName);
         statement.setString(2, message);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      }
   }
   
   public void addMemo(String projectName, RMemo memo) {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO memo(project_name, memo_date, memo_description) VALUES(?, ?, ?)");
         statement.setString(1, projectName);
         statement.setDate(2, (java.sql.Date) memo.getDate());
         statement.setString(3, memo.getDescription());
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
   }
   
   public void addMember(RMember member)  {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO member(member_email, member_name) VALUES(?, ?)");
         PreparedStatement statement1 = connection.prepareStatement("INSERT INTO user_(user_email, user_name) VALUES(?, ?)");
         statement.setString(1, member.getEmail());
         statement.setString(2, member.getName());
         statement1.setString(1, member.getEmail());
         statement1.setString(2, member.getName());
         statement.executeUpdate();
         statement1.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
   }
   
   public void addParticipation(String projectName, RemoteMemberInterface member) {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO participation(project_name, member_email) VALUES(?, ?)");
         PreparedStatement statement1 = connection.prepareStatement("INSERT INTO registration(project_name, user_email) VALUES(?, ?)");
         statement.setString(1, projectName);
         statement.setString(2, member.getEmail());
         statement1.setString(1, projectName);
         statement1.setString(2, member.getEmail());
         statement.executeUpdate();
         statement1.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
   }
   
   public void updateProject(String projectName, String newProjectName) {
     try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE project SET project_name = ? WHERE project_name = ?");
         statement.setString(1, newProjectName);
         statement.setString(2, projectName);
         statement.executeUpdate();
      }
     catch (SQLException e)
     {
        try {
           connection.close();
        } catch (SQLException e1) {
           e1.printStackTrace();
        }
     }
   }
   
   public void updateMemo(String projectName, RMemo memo, String newMemoName, Date newMemoDate)  {
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ?, memo_description = ?  WHERE memo_description = ? AND project_name = ?");
         statement.setDate(1, (java.sql.Date) newMemoDate);
         statement.setString(2, newMemoName);
         statement.setString(3, memo.getDescription());
         statement.setString(4, projectName);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
   }
   
   public void updateMemoName(String projectName, RMemo memo, String newMemoName) {
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_description = ? WHERE memo_description = ? AND project_name = ?");
         statement.setString(1, newMemoName);
         statement.setString(2, memo.getDescription());
         statement.setString(3, projectName);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
   }
   
   public void updateMemoDate(String projectName, RMemo memo, Date newMemoDate)  {
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ? WHERE memo_description = ? AND project_name = ?");
         statement.setDate(1, (java.sql.Date) newMemoDate);
         statement.setString(2, memo.getDescription());
         statement.setString(3, projectName);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
   }
   
   public void deleteProject(String projectName) {
      try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM project WHERE project_name = ?");
         statement.setString(1, projectName);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      }
   }
   
   public void deleteMemo(String projectName, RemoteMemoInterface memo) {
      try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM memo WHERE memo_description = ? AND project_name = ?");
         statement.setString(1, memo.getDescription());
         statement.setString(2, projectName);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } catch (RemoteException e) {
         e.printStackTrace();
      }
   }
   
   public void deleteParticipant(String projectName, RemoteMemberInterface member) {
     try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM participation WHERE member_email = ? AND project_name = ?");
         PreparedStatement statement1 = connection.prepareStatement("DELETE FROM registration WHERE user_email = ? AND project_name = ?");
         statement.setString(1, member.getEmail());
         statement.setString(2, projectName);
         statement1.setString(1, member.getEmail());
         statement1.setString(2, projectName);
         statement.executeUpdate();
      }
      catch (SQLException e)
      {
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
