package ro.ace.ucv;

import java.io.Serializable;

/**
 * 
 * A task represents a program and its minimum available requirements from the system. It also contains the intervals the program gets as parameters..
 *
 */
public class Task implements Serializable {

	private String fileName;
	private Long freeSpaceNecessary;
	private Long cpuTime;
	private Long freeMemoryNeeded;
	private String start;
	private String end;

	/** SETTER **/
	public void setCpuTime(Long cpuTime) {
		this.cpuTime = cpuTime;
	}

	/** SETTER **/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** SETTER **/
	public void setFreeMemoryNeeded(Long freeMemoryNeeded) {
		this.freeMemoryNeeded = freeMemoryNeeded;
	}

	/** SETTER **/
	public void setFreeSpaceNecessary(Long freeSpaceNecessary) {
		this.freeSpaceNecessary = freeSpaceNecessary;
	}
	
	/** GETTER **/
	public Long getCpuTime() {
		return cpuTime;
	}

	/** GETTER **/
	public String getFileName() {
		return fileName;
	}

	/** GETTER **/
	public String getStart() {
		return start;
	}
	/** GETTER **/
	public String getEnd() {
		return end;
	}
	
	
	/** GETTER **/
	public Long getFreeMemoryNeeded() {
		return freeMemoryNeeded;
	}

	/** GETTER **/
	public Long getFreeSpaceNecessary() {
		return freeSpaceNecessary;
	}
	

	/** SETTER **/
	public void setStart(String param1) {
		this.start = param1;
	}
	
	/** SETTER **/
	public void setEnd(String param2) {
		this.end = param2;
	}

	@Override
	public String toString() {

		return "CPU: " + cpuTime + "\n" + "FreeMem:" + freeMemoryNeeded + "\n"
				+ "FreeSpace:" + freeSpaceNecessary + "\n" + "FileName"
				+ fileName;
	}

}
