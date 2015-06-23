package ro.ace.ucv.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * The User Interface for the Server part of the application.
 *
 */
public class ServerGUI extends JFrame {
	private JTextArea consola;
	private JScrollPane sp;
	
	/**
	 * Initializes the frame and the components.
	 */
	public void init() {
		setTitle("Server");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(500, 500);
		setResizable(false);
		JPanel panou = new JPanel();
		consola = new JTextArea(10, 30);
		consola.setEditable(false);

		 sp = new JScrollPane(consola);
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				consola.setText("");
			}
		});
		panou.add(sp);
		panou.add(clearButton);

		add(panou);
		addLine("Server started");
		refresh();
	}

	/**
	 * Refreshes the component on the frame by revalidating and repainting them.
	 */
	private void refresh() {
		revalidate();
		repaint();
	}
	
	/**
	 * Constructor used to initialise the JFrame.
	 */
	public ServerGUI() {
		init();
	}

	/**
	 * 
	 * Appends a line to the console(JTextArea)
	 * @param consoleMessage the line to append to the console.
	 */
	public void addLine(String consoleMessage) {

		consola.append(consoleMessage + "\n");
		consola.setCaretPosition(consola.getDocument().getLength());
	}
}
