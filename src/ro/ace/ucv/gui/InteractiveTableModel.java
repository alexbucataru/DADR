package ro.ace.ucv.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import ro.ace.ucv.Task;

/**
 *
 * Implementation for the table used to display all the available tasks.
 *
 */
public class InteractiveTableModel extends AbstractTableModel {
    private static final int FILE_NAME = 0;
    private static final int FREE_MEMORY = 1;
    private static final int CPU_TIME = 2;
    private static final int  FREE_SPACE = 3;

    private String[] columnNames;
    private Vector dataVector;

    /**
     * Constructor
     * @param columnNames the column names of the table.
     */
    public InteractiveTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        dataVector = new Vector();
    }

    /** GETTER  */
    public String getColumnName(int column) {
        return columnNames[column];
    }
    /** GETTER  */
    public Object getValueAt(int row, int column) {
        Task task = (Task)dataVector.get(row);
        switch (column) {
            case FILE_NAME:
               return task.getFileName();
            case FREE_MEMORY:
               return task.getFreeMemoryNeeded();
            case CPU_TIME:
               return task.getCpuTime();
            case FREE_SPACE:
                return task.getFreeSpaceNecessary();
            default:
               return new Object();
        }
    }
    
    /** GETTER  */
    public int getRowCount() {
        return dataVector.size();
    }

    /** GETTER  */
    public int getColumnCount() {
        return columnNames.length;
    }
    
    
    /**
     * Adds a task to the table.
     * @param task the task to be added in the table.
     */
    public void addTask(Task task) {
        dataVector.add(task);
        fireTableRowsInserted(
           dataVector.size() - 1,
           dataVector.size() - 1);
    }

}