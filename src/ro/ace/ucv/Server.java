package ro.ace.ucv;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import ro.ace.ucv.gui.ServerGUI;

/**
 * 
 * Class implementation for the server logic of the application.
 *
 */
public class Server {
	private static int i = 0;

	/**
	 * Initializes the server GUI and launches a thread which handles the logic
	 * part of th server.
	 * 
	 * @param argv
	 *            the arguments passed in main, if any
	 * @throws Exception
	 */
	public static void main(String argv[]) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(4022);
		ServerGUI svGui = new ServerGUI();
		Responder h = new Responder(svGui);
		while (true) {

			Socket connectionSocket = welcomeSocket.accept();
			svGui.addLine("Client Conected - " + i);
			h.setClientName("Client Nr" + i);
			i++;
			Thread t = new Thread(new MyServer(h, connectionSocket));
			t.start();

		}
	}
}

/**
 * A thread for handling a client.
 */
class MyServer implements Runnable {

	private Responder responder;
	private Socket connectionSocket;

	/**
	 * Constructor
	 * 
	 * @param responder
	 *            a model for handling of a client
	 * @param connectionSocket
	 *            Socket used for the connection
	 */
	public MyServer(Responder responder, Socket connectionSocket) {
		this.responder = responder;
		this.connectionSocket = connectionSocket;
	}

	@Override
	public void run() {
		while (responder.responderMethod(connectionSocket)) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		try {
			connectionSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null,
					ex);
		}

	}

}

/**
 * Class for handling a client.
 *
 */
class Responder {
	private ServerGUI svGui;
	private String clientName;

	/**
	 * Constructor
	 */
	public Responder(ServerGUI svGui) {
		this.svGui = svGui;
	}

	/** SETTER */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * Method representing the logic for handling a clien: recieve system
	 * information, send available tasks, receive file name chosen, send program
	 * and receive response from the client.
	 * 
	 * @param connectionSocket
	 *            Sockket used for the connection
	 * @return true if operations are successful.
	 */
	public boolean responderMethod(Socket connectionSocket) {
		try {

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			String comand = inFromClient.readLine();
			if (!comand.equals("<result>")) {
				svGui.addLine("Reving computer information...");

				String freeMemory = inFromClient.readLine();
				String cpuTime = inFromClient.readLine();
				String freeSpace = inFromClient.readLine();

				ObjectOutputStream oos;
				// if client process terminates it get null, so close connection
				if (freeMemory == null || cpuTime == null || freeSpace == null) {

					return false;
				} else {
					System.out.println(clientName + "Free memory received..."
							+ freeMemory);
					System.out.println(clientName + "Cpu Time recived..."
							+ cpuTime);
					System.out.println(clientName + "Free space received..."
							+ freeSpace);
					List<Task> tasks = ProgramList.getProgramList(freeMemory,
							cpuTime, freeSpace);

					oos = new ObjectOutputStream(
							connectionSocket.getOutputStream());
					oos.writeObject(tasks);
					svGui.addLine(clientName + "List of task sended...");

				}
				String fileName = inFromClient.readLine();

				Task task = ProgramList.getTaskFromName(fileName);
				Properties prop = new Properties();
				String configPath = ProgramList.serverPath + File.separator
						+ fileName + File.separator + fileName + ".config";
				InputStream inputStream = new FileInputStream(new File(
						configPath));
				prop.load(inputStream);
				String param1 = prop.getProperty("param1");

				String param2 = prop.getProperty("param2");
				svGui.addLine(clientName + "Sending params..");

				String end = ""
						+ (Integer.parseInt(param1) + Integer.parseInt(param2) / 10);
				task.setStart(param1);
				task.setEnd(end);

				prop.setProperty("param1", end);

				prop.store(new FileOutputStream(new File(configPath)),
						"Interval Changed");

				outToClient.writeBytes(task.getStart() + "\n");
				outToClient.writeBytes(task.getEnd() + "\n");

				File myFile = new File(ProgramList.serverPath + File.separator
						+ fileName + File.separator + fileName + ".class");
				byte[] mybytearray = new byte[(int) myFile.length()];

				FileInputStream fis = null;

				try {
					fis = new FileInputStream(myFile);
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				}
				BufferedInputStream bis = new BufferedInputStream(fis);
				System.out.println("aaaa");
				svGui.addLine(clientName + "Sending file...");
				bis.read(mybytearray, 0, mybytearray.length);
				outToClient.write(mybytearray, 0, mybytearray.length);
				svGui.addLine(clientName + "File send...");
				outToClient.flush();
				outToClient.close();

				connectionSocket.close();
				// File sent, exit the main method
				bis.close();
				oos.close();
				connectionSocket.close();
				return true;
			} else {
				String fileName = inFromClient.readLine();
				String result = inFromClient.readLine();
				svGui.addLine(clientName + "Reciveing result..." + result);

				Properties prop = new Properties();
				String configPath = ProgramList.serverPath + File.separator
						+ fileName + File.separator + fileName + ".config";
				InputStream inputStream = new FileInputStream(new File(
						configPath));
				prop.load(inputStream);

				prop.setProperty("param1", "" + 0);
				prop.setProperty("result", result);

				prop.store(new FileOutputStream(new File(configPath)),
						"Interval Changed");

				connectionSocket.close();
				return true;
			}
		} catch (SocketException e) {
			svGui.addLine(clientName + " Disconected");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}