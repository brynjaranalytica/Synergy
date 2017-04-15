package client.view;

import javax.swing.*;

/**
 * Created by lenovo on 4/13/2017.
 */
public abstract class Window extends JPanel implements ViewInterface {
    protected static View view;

    Window() {
        initComponents();
        setEventHandlers();
    }

 }
