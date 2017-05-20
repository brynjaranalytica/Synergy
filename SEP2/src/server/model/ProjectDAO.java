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

import server.remote_business_enitities.RCalendar;
import server.remote_business_enitities.RChat;
import server.remote_business_enitities.RMember;
import server.remote_business_enitities.RMemo;
import server.remote_business_enitities.RProject;
import shared.remote_business_interfaces.RemoteMemberInterface;
import shared.remote_business_interfaces.RemoteMemoInterface;
import shared.remote_business_interfaces.RemoteProjectInterface;

public class ProjectDAO
{
   private static ProjectDAO instance;
   private final Connection connection;

   public Connection getConnection() {
      return connection;
   }

   public static synchronized ProjectDAO getInstance() throws SQLException {
      if (instance == null) {
         instance = new ProjectDAO();
      }
      return instance;
   }
   
   private ProjectDAO() throws SQLException {
      DriverManager.registerDriver(new Driver());
      connection = DriverManager
              .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
   }
   
   public ArrayList<RemoteProjectInterface> readAllProjects() throws SQLException, RemoteException {

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
      return projects;
   }
   
   public ArrayList<RemoteProjectInterface> readAllProjectsForUser(String user_email) throws SQLException, RemoteException {
      ArrayList<RemoteProjectInterface> projects = new ArrayList<RemoteProjectInterface>();
      try
      {
         PreparedStatement statement = connection.prepareStatement("SELECT project_name FROM registration WHERE user_email = ?");
         statement.setString(1, user_email);
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
         try
         {
            connection.close();
         }
         catch (SQLException e2)
         {
            e2.printStackTrace();
         }
      }
      return projects;
   }
   
   public RProject readProject(String projectName) throws SQLException, RemoteException {
       RProject project = new RProject(projectName);
      try
      {
         PreparedStatement statement = connection.prepareStatement("SELECT project_name FROM project WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery(); 
         while (result.next())
         {
            project.setChat(readChat(result.getString("project_name")));
            project.setCalendar(readCalendar(result.getString("project_name")));
            project.setMembers(readParticipants(result.getString("project_name")));
         }
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } return project;
   }
   
   public  RChat readChat(String projectName) throws SQLException, RemoteException {
      RChat chat = new RChat();
      try {
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM message WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery(); 
         while (result.next()) {
            chat.addMessage(result.getString("message_body"));
        }
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      }return chat;
   }
   
   public RCalendar readCalendar(String projectName) throws SQLException, RemoteException {
       RCalendar calendar = new RCalendar();
      try {
         PreparedStatement statement = connection.prepareStatement("SELECT memo_date, memo_description FROM memo WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery();
         while (result.next()) {
            RemoteMemoInterface memo = new RMemo(result.getDate("memo_date"), result.getString("memo_description"));
            calendar.addMemo(memo);
        }
      }
      catch (SQLException e)
      {
         try {
            connection.close();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } return calendar;
   }
   
   public ArrayList<RemoteMemberInterface> readMembers() throws SQLException, RemoteException {
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
      } return members;
   }
   
   public ArrayList<RemoteMemberInterface> readParticipants(String projectName) throws SQLException, RemoteException {
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
      }
      return members;
   }
   
   public void addProject(String projectName) throws SQLException {
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
   
   public void addMessage(String projectName, String message) throws SQLException {
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
   
   public void addMemo(String projectName, Date memoDate, String memoDescription) throws SQLException {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO memo(project_name, memo_date, memo_description) VALUES(?, ?, ?)");
         statement.setString(1, projectName);
         statement.setDate(2, (java.sql.Date) memoDate);
         statement.setString(3, memoDescription);
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
   
   public void addMember(String memberEmail, String memberName) throws SQLException {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO member(member_email, member_name) VALUES(?, ?)");
         PreparedStatement statement1 = connection.prepareStatement("INSERT INTO user_(user_email, user_name) VALUES(?, ?)");
         statement.setString(1, memberEmail);
         statement.setString(2, memberName);
         statement1.setString(1, memberEmail);
         statement1.setString(2, memberName);
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
      }
   }
   
   public void addParticipation(String projectName, String memberEmail) {
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO participation(project_name, member_email) VALUES(?, ?)");
         PreparedStatement statement1 = connection.prepareStatement("INSERT INTO registration(project_name, user_email) VALUES(?, ?)");
         statement.setString(1, projectName);
         statement.setString(2, memberEmail);
         statement1.setString(1, projectName);
         statement1.setString(2, memberEmail);
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
      }
   }
   
   public void updateProject(String projectName, String newProjectName) throws SQLException {
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
   
   public void updateMemo(String projectName, String memoName, String newMemoName, Date newMemoDate) throws SQLException {
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ?, memo_description = ?  WHERE memo_description = ? AND project_name = ?");
         statement.setDate(1, (java.sql.Date) newMemoDate);
         statement.setString(2, newMemoName);
         statement.setString(3, memoName);
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
      }
   }
   
   public void updateMemoName(String projectName, String memoName, String newMemoName) throws SQLException {
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_description = ? WHERE memo_description = ? AND project_name = ?");
         statement.setString(1, newMemoName);
         statement.setString(2, memoName);
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
      }
   }
   
   public void updateMemoDate(String projectName, String memoName, Date newMemoDate) throws SQLException {
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ? WHERE memo_description = ? AND project_name = ?");
         statement.setDate(1, (java.sql.Date) newMemoDate);
         statement.setString(2, memoName);
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
      }
   }
   
   public void deleteProject(String projectName) throws SQLException {
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
   
   public void deleteMemo(String projectName, String memoName) throws SQLException {
      try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM memo WHERE memo_description = ? AND project_name = ?");
         statement.setString(1, memoName);
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
   
   public void deleteParticipant(String projectName, String memberEmail) throws SQLException {
     try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM participation WHERE member_email = ? AND project_name = ?");
         PreparedStatement statement1 = connection.prepareStatement("DELETE FROM registration WHERE user_email = ? AND project_name = ?");
         statement.setString(1, memberEmail);
         statement.setString(2, projectName);
         statement1.setString(1, memberEmail);
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
      }
   }

}
