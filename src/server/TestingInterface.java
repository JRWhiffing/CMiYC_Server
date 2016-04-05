package server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A graphical interface to send packets to the server (to debug and test the server) and to
 * print responses
 * 
 * @author James
 *
 */

public class TestingInterface extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public TestingInterface(int clientID) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestingInterface frame = new TestingInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestingInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
