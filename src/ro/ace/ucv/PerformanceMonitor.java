package ro.ace.ucv;

import java.io.File;
import java.lang.management.ManagementFactory;

/**
 * Class implementation responsible of getting the system informations.
 *
 */
public class PerformanceMonitor {
	private static com.sun.management.OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();

	/**
	 * GETTER
	 * @return the available free memory in MB.
	 */
	public static long getFreeMemoryInMb() {

		long freeMemory = operatingSystem.getFreePhysicalMemorySize();

		return freeMemory / 1024 / 1024;
	}

	/**
	 * GETTER
	 * @return the Cpu Time in Nanosecons.
	 */
	public static Long getCpuTimeInNanoSeconds() {
		return operatingSystem.getProcessCpuTime();
	}

	/**
	 * GETTER
	 * @return the remaining free space in GB.
	 */
	public static Long getFreeSpaceInGB() {

		File file = new File(System.getProperty("user.dir"));
		long freeSpace = file.getFreeSpace(); // unallocated / free disk space
												// in bytes.
		return freeSpace / Math.round(Math.pow(1024, 3));
	}
}