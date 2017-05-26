package client.view.windows;

import client.view.windows.View;
import client.view.windows.ViewInterface;

import javax.swing.*;


public abstract class Window extends JPanel implements ViewInterface {

	private static final long serialVersionUID = 1L;
	protected static View view;

    public Window() {
        initComponents();
        setEventHandlers();
    }

 }
