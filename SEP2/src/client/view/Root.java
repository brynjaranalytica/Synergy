package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Root extends Window {

	private static final long serialVersionUID = 1L;

	public Root() {
	}
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
        add(new JLabel("THIS IS ROOT WINDOW"));
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
    public void showRoot() {

    }
}
