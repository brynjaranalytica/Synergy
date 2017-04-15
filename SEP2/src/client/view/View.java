package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static client.view.Window.view;


public class View extends JFrame implements ViewInterface, WindowConstants{
    //private final HashMap<String, Window> windows = new HashMap<>();

    private final JDesktopPane desktopPane = new JDesktopPane();
    private Window currentWindow;

    public View() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                System.exit(0);
            }
        });
        //setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("../resources/icon.png")));
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBounds(100, 100, 420, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setCurrentWindow(LOGIN);
        setVisible(true);


        desktopPane.setLayout(new CardLayout(0,0));
        getContentPane().add(desktopPane, BorderLayout.CENTER);
        view = this;
    }


    @Override
    public void showLogin() {
        currentWindow.showLogin();
    }

    @Override
    public void showMain() {
        currentWindow.showMain();
    }

    @Override
    public void setEventHandlers() {
        currentWindow.setEventHandlers();
    }

    @Override
    public void initComponents() {
        currentWindow.initComponents();
    }

    @Override
    public void loadData() {
        currentWindow.loadData();
    }


    public void setCurrentWindow(Window window) {
        if(desktopPane.getComponentCount() == 0) {
            window.loadData();
            window.setVisible(true);
            desktopPane.add(window);
            currentWindow = window;
        }
        else{
            desktopPane.getComponent(0).setVisible(false);
            desktopPane.removeAll();
            window.loadData();
            window.setVisible(true);
            desktopPane.add(window);
            currentWindow = window;
        }
    }


}
