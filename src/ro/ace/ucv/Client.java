package ro.ace.ucv;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import ro.ace.ucv.gui.ClientListener;

/**
 *  The class implementation for the client logic: connecting to server, recieveing file from server, executing file and sending responce.
 */
public class Client {
	private Socket socket;
	private int serverPort = 4022;
	private final String serverUrl = "localhost";
	private List<Task> tasks;
	private BufferedReader fromServer;
	private PrintWriter toServer;
	private String programUrl = "G:\\eclipse\\workspace\\ParalelComputing\\received";
	private InetAddress host;
	private ClientListener clientListener;

	
	/**
	 * Constructor
	 * @param clientListener listner for the GUI part of the client.  
	 */
	public Client(ClientListener clientListener) {
		this.clientListener = clientListener;

	}

	/**
	 * Connects to the server, sends the system information and receives the available tasks.
	 */
	public void run() {
		try {
			host = InetAddress.getByName(serverUrl);

			socket = new Socket(host, serverPort);
			// Socket socket = new Socket("127.0.0.1", serverPort);
			clientListener.connect();
			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());
			toServer = new PrintWriter(socket.getOutputStream(), true);
			fromServer = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			toServer.println("<read>");
			toServer.println(PerformanceMonitor.getFreeMemoryInMb());
			toServer.println(PerformanceMonitor.getCpuTimeInNanoSeconds());
			toServer.println(PerformanceMonitor.getFreeSpaceInGB());

			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());

			try {
				tasks = (List<Task>) ois.readObject();
				for (Task task : tasks) {
					System.out.println(task.toString());
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			clientListener.fillTable(tasks);
		} catch (UnknownHostException ex) {
			clientListener.error();
			ex.printStackTrace();
		} catch (IOException e) {
			clientListener.error();
		}
	}

	/**
	 * Closes the conenction to the server.
	 * @throws IOException
	 */
	public void closeConection() throws IOException {
		toServer.close();
		fromServer.close();
		socket.close();
	}

	/**
	 * Asks the server for the java file with the name given as parameter.
	 * 
	 * @param fileName the name of the program to recieve.
	 * @throws IOException
	 */
	public void requestFile(String fileName) throws IOException {
		toServer.println(fileName);
		String param1 = fromServer.readLine();
		String param2 = fromServer.readLine();
		reciveFile(fileName, param1, param2);
	}

	/**
	 * Executes the file received from the server.
	 * 
	 * @param fileName the name of the file
	 * @param param1 first parameter received from the server.
	 * @param param2 second parameter received from the server.
	 */
	private void executeFile(final String fileName, String param1, String param2) {
		final Process process;
		try {
			process = Runtime.getRuntime().exec(
					"java -cp " + programUrl + " " + fileName + " " + param1
							+ " " + param2);

			final BufferedReader stdInput = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			final BufferedReader stdError = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						String s = null;
						while ((s = stdInput.readLine()) != null) {
							System.out.println(s);
							if (s != null && !s.equals("null")) {
								socket = new Socket(host, serverPort);
								sendResponse(fileName, s);
							}
							stdInput.close();
							stdError.close();
							return;
						}

						// read any errors from the attempted command
						System.out
								.println("Here is the standard error of the command (if any):\n");
						while ((s = stdError.readLine()) != null) {
							System.out.println(s);
							stdInput.close();
							stdError.close();
							return;
						}

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Sends the result to the server.
	 * 
	 * @param fileName the name of the executed program. 
	 * @param result the result computed after executing the program.
	 */
	private void sendResponse(String fileName, String result) {
		try {
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			pw.println("<result>");
			pw.println(fileName);
			pw.println(result);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives the file from the server
	 * 
	 * @param fileName the name of the program.
	 * @param param1 the first parameter from the server.
	 * @param param2 the second parameter from the server.
	 */
	private void reciveFile(String fileName, String param1, String param2) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			InputStream is = socket.getInputStream();

			if (is != null) {

				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				byte[] aByte = new byte[1];
				int bytesRead;

				fos = new FileOutputStream(programUrl + File.separator
						+ fileName + ".class");
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(aByte, 0, aByte.length);

				do {
					baos.write(aByte);
					bytesRead = is.read(aByte);
				} while (bytesRead != -1);

				bos.write(baos.toByteArray());
				bos.flush();
				bos.close();
			}
			executeFile(fileName, param1, param2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}