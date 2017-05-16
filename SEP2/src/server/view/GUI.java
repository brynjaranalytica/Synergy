package server.view;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import server.controller.ServerController;

import java.awt.Component;
import javax.swing.JTextPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

public class GUI{

	private JFrame frmSynergy;
	private  JMenuItem mntmExitServer;
	@SuppressWarnings("unused")
	private ServerController serverController;

	public GUI(ServerController sc) {
		initComponents();
		createEvents();
		serverController = sc;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initComponents() {
		frmSynergy = new JFrame();
		frmSynergy.getContentPane().setBackground(Color.WHITE);
	
		frmSynergy.setResizable(false);
		frmSynergy.setTitle("Synergy\u00AE Server - Project Management System");
		frmSynergy.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/resources/icon.png")));
		frmSynergy.setBounds(200, 200, 700, 500);
		frmSynergy.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmSynergy.setVisible(true);
		frmSynergy.setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		frmSynergy.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmExitServer = new JMenuItem("Exit server");
		mnFile.add(mntmExitServer);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		lblNewLabel.setIcon(new ImageIcon(GUI.class.getResource("/resources/server_back_logo.jpg")));
		
		JTextPane txtpnServerIsRunning = new JTextPane();
		txtpnServerIsRunning.setEditable(false);
		txtpnServerIsRunning.setText("The Synergy server is now running on "+ServerController.getIP()+"\nThis IP-address must be used by client connections when connecting to the server over the Local Area Network.");
		GroupLayout groupLayout = new GroupLayout(frmSynergy.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 694, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtpnServerIsRunning, GroupLayout.PREFERRED_SIZE, 694, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtpnServerIsRunning, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		frmSynergy.getContentPane().setLayout(groupLayout);

	}
	
	private void createEvents(){
		//File menu: Exit server
		mntmExitServer.addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(null, "Are you sure you want to do a server shutdown?")==0){
				System.exit(0);				
			}
		} );
		//Close window: Exit server
		frmSynergy.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to do a server shutdown?")==0){
					System.exit(0);				
				}
			}
		});
		
	}

}
