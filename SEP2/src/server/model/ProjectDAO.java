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
   
   public static synchronized ProjectDAO getInstance() throws SQLException {
      if (instance == null) {
         instance = new ProjectDAO();
      }
      return instance;
   }
   
   private ProjectDAO() throws SQLException {
      DriverManager.registerDriver(new Driver());
   }
   
   public ArrayList<RemoteProjectInterface> readAllProjects() throws SQLException, RemoteException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      ArrayList<RemoteProjectInterface> projects = new ArrayList<RemoteProjectInterface>();
      try {
         PreparedStatement statement = connection.prepareStatement("SELECT * project_name FROM project");
         ResultSet result = statement.executeQuery(); 
         while (result.next()) {
            RProject remoteProject = new RProject(result.getString("project_name"));
            remoteProject.setChat(readChat(result.getString("project_name")));
            remoteProject.setCalendar(readCalendar(result.getString("project_name")));
            remoteProject.setMembers(readMembers(result.getString("project_name")));
            projects.add(remoteProject);
        }
         
      } finally {
         connection.close();
      }
      return projects;
   }
   
   public RProject readProject(String projectName) throws SQLException, RemoteException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      RProject project = new RProject(projectName);
      try
      {
         PreparedStatement statement = connection.prepareStatement("SELECT project_name FROM project WHERE project_name = ?");
         ResultSet result = statement.executeQuery(); 
         while (result.next())
         {
            project.setChat(readChat(result.getString("project_name")));
            project.setCalendar(readCalendar(result.getString("project_name")));
            project.setMembers(readMembers(result.getString("project_name")));
         }
      }
      finally
      {
         connection.close();
      }
      return project;
   }
   
   public RChat readChat(String projectName) throws SQLException, RemoteException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      RChat chat = new RChat();
      try {
         PreparedStatement statement = connection.prepareStatement("SELECT * message_body FROM message WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery(); 
         while (result.next()) {
            chat.addMessage(result.getString("message_body"));
        }
      } finally {
         connection.close();
      }
      return chat;
   }
   
   public RCalendar readCalendar(String projectName) throws SQLException, RemoteException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      RCalendar calendar = new RCalendar();
      try {
         PreparedStatement statement = connection.prepareStatement("SELECT memo_date, memo_description FROM memo WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery();
         while (result.next()) {
            RemoteMemoInterface memo = new RMemo(result.getDate("memo_date"), result.getString("memo_description"));
            calendar.addMemo(memo);
        }
      } finally {
         connection.close();
      }
      return calendar;
   }
   
   public ArrayList<RemoteMemberInterface> readMembers(String projectName) throws SQLException, RemoteException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      ArrayList<RemoteMemberInterface> members = new ArrayList<RemoteMemberInterface>();
      try
      {
         PreparedStatement statement = connection.prepareStatement("SELECT member_name, member_email, FROM member"
               + "WHERE member_email IN (SELECT member_email FROM participation"
               + "WHERE project_name = ?");
         statement.setString(1, projectName);
         ResultSet result = statement.executeQuery();
         while (result.next()) {
            RemoteMemberInterface member = new RMember(result.getString("member_email"),  result.getString("member_name"));
            members.add(member);
        }
      }
      finally
      {
         connection.close();
      }
      return members;
   }
   
   public void addMessage(String projectName, String message) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO message(project_name, message_body) VALUES = (?, ?)");
         statement.setString(1, projectName);
         statement.setString(2, message);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void addMemo(String projectName, Date memoDate, String memoDescription) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO memo(project_name, memo_date, memo_description) VALUES(?, ?, ?)");
         statement.setString(1, projectName);
         statement.setDate(2, (java.sql.Date) memoDate);
         statement.setString(3, memoDescription);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void addParticipation(String projectName, String memberEmail) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("INSERT INTO participation(project_name, member_email) VALUES(?, ?)");
         statement.setString(1, projectName);
         statement.setString(2, memberEmail);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void updateProject(String projectName, String newProjectName) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE project SET project_name = ? WHERE project_name = ?");
         statement.setString(1, newProjectName);
         statement.setString(2, projectName);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void updateMemo(String memoName, String newMemoName, Date newMemoDate) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ?, memo_description = ?  "
               + "WHERE memo_description = ?");
         statement.setDate(1, (java.sql.Date) newMemoDate);
         statement.setString(2, newMemoName);
         statement.setString(2, memoName);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void updateMemoName(String memoName, String newMemoName) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_description = ? WHERE memo_description = ?");
         statement.setString(1, newMemoName);
         statement.setString(2, memoName);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void updateMemoDate(String memoName, Date newMemoDate) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("UPDATE memo SET memo_date = ? WHERE memo_description = ?");
         statement.setDate(1, (java.sql.Date) newMemoDate);
         statement.setString(2, memoName);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void deleteProject(String projectName) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM project WHERE project_name = ?");
         statement.setString(1, projectName);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void deleteMemo(String memoName) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM memo WHERE memo_description = ?");
         statement.setString(1, memoName);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }
   
   public void deleteParticipant(String memberEmail) throws SQLException {
      Connection connection = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=synergy", "postgres", "159");
      try
      {
         PreparedStatement statement = connection.prepareStatement("DELETE FROM participation WHERE member_email = ?");
         statement.setString(1, memberEmail);
         statement.executeUpdate();
      }
      finally
      {
         connection.close();
      }
   }

}
