package ro.ace.ucv.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ro.ace.ucv.Client;
import ro.ace.ucv.Task;

/**
 * 
 * User Interface for the Client side of the application
 *
 */
public class ClientGui extends JFrame implements ClientListener {

	private JTable table;
	private InteractiveTableModel tableModel;
	private Client client;

	public static void main(String[] args) {

		new ClientGui();
	}

	
	/**
	 * The constructor initializes the Client User Interface.  
	 */
	public ClientGui() {
		init();
		client = new Client(this);
		client.run();
	}
	
	private void init() {
		setTitle("Computing time");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(800, 500);
		setResizable(false);
		JPanel panou = new JPanel();
		panou.add(new JLabel("Conecting..."));
		this.setContentPane(panou);
		refresh();
	}

	
	private void refresh() {
		revalidate();
		repaint();
	}

	private JTable createTable(List<Task> task) {

		String columnNames[] = { "File Name", "Free Space Necesary",
				"CPU TIME", "Free memory neded" };
		tableModel = new InteractiveTableModel(columnNames);
		table = new JTable();
		table.setModel(tableModel);

		return table;
	}

	@Override
	public void fillTable(List<Task> tasks) {

		final JTable table = createTable(tasks);
		JPanel panou = new JPanel();
		panou.add(new JScrollPane(table));

		for (Task task : tasks) {
			tableModel.addTask(task);
		}

		JButton button = new JButton("Start Working");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = (String) tableModel.getValueAt(
						table.getSelectedRow(), 0);
				try {
					client.requestFile(fileName);
					client = new Client(ClientGui.this);
					client.run();
				} catch (IOException e1) {
					error();
				}
			}
		});

		panou.add(button);
		this.setContentPane(panou);
		refresh();
	}

	/**
	 * The Gui displays a 'Connected' message
	 */
	@Override
	public void connect() {

		JPanel panou = new JPanel();
		panou.add(new JLabel("Conected"));
		this.setContentPane(panou);
		refresh();
	}

	/**
	 * The Gui displays a 'Error' message
	 */
	@Override
	public void error() {
		JPanel panou = new JPanel();
		panou.add(new JLabel("Error occured.."));
		this.setContentPane(panou);
		refresh();
	}
}
