package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by lenovo on 4/13/2017.
 */
public class Main extends Window {
    private JButton btnLogOut;

    @Override
    public void setEventHandlers() {
        btnLogOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                view.showLogin();
            }
        });

    }

    @Override
    public void initComponents() {
        setBackground(Color.WHITE);
        setBounds(100, 100, 420, 300);
        setLayout(new BorderLayout());
        add(new JLabel("THIS IS MAIN WINDOW"));
        btnLogOut = new JButton("Log out");
        add(btnLogOut);

    }

    @Override
    public void loadData() {

    }

    @Override
    public void showLogin() {
        view.setCurrentWindow(LOGIN);
    }

    @Override
    public void showMain() {

    }
}
