package client.view;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

public abstract class AbstractJIF extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	public AbstractJIF() {
		setFrameIcon(new ImageIcon(Root.class.getResource("/resources/iconbar.png")));
		setResizable(true);
		getContentPane().setLayout(null);
		setMaximizable(true);
		setAutoscrolls(true);
		setClosable(true);
		setVisible(true);
	}

	abstract public void loadData(Object object);

}
