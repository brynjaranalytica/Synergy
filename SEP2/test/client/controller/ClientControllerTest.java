package client.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.User;
import shared.business_entities.Memo;
import shared.business_entities.Project;
import shared.business_entities.ProjectInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Nicolai Onov on 5/28/2017.
 */
public class ClientControllerTest {

    private ClientController clientController;

    @Before
    public void setUp() {
        try {
            clientController = ClientController.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void loginTest() throws Exception {

        User user = clientController.login("mogens@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        Assert.assertEquals("User email", "mogens@via.dk", user.getiD());
        Assert.assertEquals("User name", "Mogens Bjerregaard", user.getName());
        Assert.assertTrue("User password", Arrays.equals(new char[]{'1', '2', '3', '4', '5', '6'}, user.getPass()));
        Assert.assertEquals("User phone", "4593939624", user.getPhone());

        user = clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        Assert.assertEquals("User email", "253739@via.dk", user.getiD());
        Assert.assertEquals("User name", "Nicolai Onov", user.getName());
        Assert.assertTrue("User password", Arrays.equals(new char[]{'1', '2', '3', '4', '5', '6'}, user.getPass()));
        Assert.assertEquals("User phone", "4581929966", user.getPhone());

        user = clientController.login("hazamadra@via.dk", new char[]{'h', 'a', 'z', 'a', 'm', 'a', 'd', 'r', 'a'});
        Assert.assertEquals("User email", "hazamadra@via.dk", user.getiD());
        Assert.assertEquals("User name", "Eugeniu Maloman", user.getName());
        Assert.assertTrue("User password", Arrays.equals(new char[]{'h', 'a', 'z', 'a', 'm', 'a', 'd', 'r', 'a'}, user.getPass()));
        Assert.assertEquals("User phone", "4591112919", user.getPhone());

        user = clientController.login("hazamadra@via.dk", new char[]{'h', 'a', 'z', 'a', 'm', 'a', 'd', 'r', 'a', 'a'});
        Assert.assertEquals("User object", null, user);
        user = clientController.login("hazamadra@via.dkKKK", new char[]{'h', 'a', 'z', 'a', 'm', 'a', 'd', 'r', 'a', 'a'});
        Assert.assertEquals("User object", null, user);

        user = clientController.login("mogenS@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        Assert.assertEquals("User object", null, user);
        user = clientController.login("mogens@via.dk", new char[]{'1', '2', '3', '4', '5', '6', '9'});
        Assert.assertEquals("User object", null, user);
    }

    @Test
    public void getProjectNames() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        ArrayList<String> projectNamesFromModel1 = clientController.getProjectNames();
        Assert.assertEquals("Autobus", projectNamesFromModel1.get(0));
        Assert.assertEquals("Synergy", projectNamesFromModel1.get(1));
        Assert.assertEquals("project 2", projectNamesFromModel1.get(2));

        clientController.login("mogens@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        ArrayList<String> projectNamesFromModel2 = clientController.getProjectNamesFromServer();
        Assert.assertEquals("Synergy", projectNamesFromModel2.get(0));

        clientController.login("hazamadra@via.dk", new char[]{'h', 'a', 'z', 'a', 'm', 'a', 'd', 'r', 'a'});
        ArrayList<String> projectNamesFromModel3 = clientController.getProjectNamesFromServer();
        Assert.assertEquals("Autobus", projectNamesFromModel3.get(0));

    }

    @Test
    public void getProjectNamesFromServer() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        ArrayList<String> projectNamesFromServer1 = clientController.getProjectNamesFromServer();
        Assert.assertEquals("Autobus", projectNamesFromServer1.get(0));
        Assert.assertEquals("Synergy", projectNamesFromServer1.get(1));
        Assert.assertEquals("project 2", projectNamesFromServer1.get(2));

        clientController.login("mogens@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        ArrayList<String> projectNamesFromServer2 = clientController.getProjectNamesFromServer();
        Assert.assertEquals("Synergy", projectNamesFromServer2.get(0));

        clientController.login("hazamadra@via.dk", new char[]{'h', 'a', 'z', 'a', 'm', 'a', 'd', 'r', 'a'});
        ArrayList<String> projectNamesFromServer3 = clientController.getProjectNamesFromServer();
        Assert.assertEquals("Autobus", projectNamesFromServer3.get(0));
    }

    @Test
    public void getProjectFromServer() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        Project project1 = ClientController.getProjectFromServer("Autobus");
        Project project2 = ClientController.getProjectFromServer("Synergy");
        Project project3 = ClientController.getProjectFromServer("project 2");

        Assert.assertEquals("Autobus", project1.getName());
        Assert.assertEquals("Synergy", project2.getName());
        Assert.assertEquals("project 2", project3.getName());
    }

    @Test
    public void getProjectFromModel() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        ProjectInterface project1 = clientController.getProjectFromModel("Autobus");
        ProjectInterface project2 = clientController.getProjectFromModel("Synergy");
        ProjectInterface project3 = clientController.getProjectFromModel("project 2");

        Assert.assertEquals("Autobus", project1.getName());
        Assert.assertEquals("Synergy", project2.getName());
        Assert.assertEquals("project 2", project3.getName());
    }

    @Test
    public void sendChatMessage() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        clientController.sendChatMessage("Autobus", "hi, Autobus team!");
        clientController.sendChatMessage("Synergy", "hi, Synergy team!");
        clientController.sendChatMessage("project 2", "hi, project 2 team!");

        ArrayList<String> listOfMessages1 = clientController.getProjectFromModel("Autobus").getChat().getListOfMessages();
        Assert.assertEquals("hi, Autobus team!", listOfMessages1.get(listOfMessages1.size() - 1));

        ArrayList<String> listOfMessages2 = clientController.getProjectFromModel("Synergy").getChat().getListOfMessages();
        Assert.assertEquals("hi, Synergy team!", listOfMessages2.get(listOfMessages2.size() - 1));

        ArrayList<String> listOfMessages3 = clientController.getProjectFromModel("project 2").getChat().getListOfMessages();
        Assert.assertEquals("hi, project 2 team!", listOfMessages3.get(listOfMessages3.size() - 1));
    }

    @Test
    public void addMemo() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        clientController.addMemo("Autobus", new Date(),"Autobus skype meeting!");
        clientController.addMemo("Synergy", new Date(), "Synergy skype meeting!");
        clientController.addMemo("project 2", new Date(),"project 2 skype meeting!");

        ArrayList<Memo> listOfEvents1 = clientController.getProjectFromModel("Autobus").getCalendar().getMemos();
        Assert.assertEquals("Autobus skype meeting!", listOfEvents1.get(listOfEvents1.size() - 1).getDescription());

        ArrayList<Memo> listOfEvents2 = clientController.getProjectFromModel("Synergy").getCalendar().getMemos();
        Assert.assertEquals("Synergy skype meeting!", listOfEvents2.get(listOfEvents2.size() - 1).getDescription());

        ArrayList<Memo> listOfEvents3 = clientController.getProjectFromModel("project 2").getCalendar().getMemos();
        Assert.assertEquals("project 2 skype meeting!", listOfEvents3.get(listOfEvents3.size() - 1).getDescription());
    }

    @Test
    public void addProject() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        clientController.addProject(new Project("new project 1"));
        clientController.addProject(new Project("new project 2"));
        clientController.addProject(new Project("new project 3"));

        ProjectInterface newProject1 = ClientController.getProjectFromServer("new project 1");
        ProjectInterface newProject2 = ClientController.getProjectFromServer("new project 2");
        ProjectInterface newProject3 = ClientController.getProjectFromServer("new project 3");

        Assert.assertEquals("new project 1", newProject1.getName());
        Assert.assertEquals("new project 2", newProject2.getName());
        Assert.assertEquals("new project 3", newProject3.getName());
    }

    @Test
    public void deleteProject() throws Exception {
        clientController.login("253739@via.dk", new char[]{'1', '2', '3', '4', '5', '6'});
        clientController.deleteProject("new project 1");
        clientController.deleteProject("new project 2");
        clientController.deleteProject("new project 3");

        ProjectInterface newProject1 = ClientController.getProjectFromServer("new project 1");
        ProjectInterface newProject2 = ClientController.getProjectFromServer("new project 2");
        ProjectInterface newProject3 = ClientController.getProjectFromServer("new project 3");

        Assert.assertEquals(null, newProject1);
        Assert.assertEquals(null, newProject2);
        Assert.assertEquals(null, newProject3);
    }



}