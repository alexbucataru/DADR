package ro.ace.ucv.gui;

import java.util.List;

import ro.ace.ucv.Client;
import ro.ace.ucv.Task;

/**
 * 
 * Interface used to display the list of projects for the Client GUI.
 * @see ClientGui
 */
public interface ClientListener {

	/**
	 * Fills the Client GUI with all the available tasks (projects)
	 * @see ClientGui
	 * @see Client 
	 * @param task the list of available tasks 
	 */
	
	void fillTable(List<Task> task);
	
	/**
	 * Called if connection is successful.
	 */
	void connect();
	
	/**
	 * Called if connection is not successful.
	 */
	void error();
}
