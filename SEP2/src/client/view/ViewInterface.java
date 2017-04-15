package client.view;

import client.controller.ClientController;

/**
 * Created by lenovo on 4/13/2017.
 */
public interface ViewInterface {
    ClientController controller = ClientController.getInstance();

    Window LOGIN = new Login();
    Window ROOT = new Root();

    void showLogin();
    void showRoot();
    void setEventHandlers();
    void initComponents();
    void loadData();

}
