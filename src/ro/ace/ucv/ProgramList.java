package ro.ace.ucv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class implementation responsible with handling the programs necessary minimum
 * requirements and retrieving the ones that the user can execute.
 * 
 */
public class ProgramList {

	private static List<Task> tasks = new ArrayList<Task>();
	public static final String serverPath = "G:\\eclipse\\workspace\\ParalelComputing\\task";

	static {
		File file = new File(serverPath);
		File[] files = file.listFiles();

		for (File fileTask : files) {
			Task task = new Task();
			String taskName = serverPath + File.separator + fileTask.getName();

			File configFile = null;
			for (File filesTask : fileTask.listFiles()) {
				if (filesTask.getName().contains(".config")) {
					configFile = filesTask;
					break;
				}
			}
			task.setFileName(fileTask.getName());

			Properties prop = new Properties();
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(configFile);
				if (inputStream != null) {
					prop.load(inputStream);
					String freeSpaceNecessary = prop
							.getProperty("freeSpaceNecessary");
					String cpuTime = prop.getProperty("cpuTime");
					String freeMemoryNeeded = prop
							.getProperty("freeMemoryNeeded");

					task.setCpuTime(Long.parseLong(cpuTime));
					task.setFreeMemoryNeeded(Long.parseLong(freeMemoryNeeded));
					task.setFreeSpaceNecessary(Long
							.parseLong(freeSpaceNecessary));

					tasks.add(task);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Returns the programs that the client can run based on the specifications given as parameters
	 * @param freeMemory the free memory of the client in MB.
	 * @param cpuTime the cpu time of the client in nanoseconds.
	 * @param freeSpace the free space of the client in GB.
	 * @return
	 */
	public static List<Task> getProgramList(String freeMemory, String cpuTime,
			String freeSpace) {

		Long freeMemoryLong = Long.parseLong(freeMemory);
		Long cpuTimeLong = Long.parseLong(cpuTime);
		Long freeSpaceLong = Long.parseLong(freeSpace);

		List<Task> avabileTask = new ArrayList<Task>();
		for (Task task : tasks) {

			if (freeMemoryLong > task.getFreeMemoryNeeded()
					&& cpuTimeLong > task.getCpuTime()
					&& freeSpaceLong > task.getFreeMemoryNeeded()) {
				avabileTask.add(task);
			}
		}
		return avabileTask;
	}

	/**
	 * Checks if a task with the name given as parameter exists.
	 * 
	 * @param fileName the name of the task
	 * @return true if it finds one, false otherwise.
	 */
	public static Task getTaskFromName(String fileName) {
		for (Task task : tasks) {
			if (task.getFileName().equals(fileName)) {
				return task;
			}
		}
		return null;
	}
}
