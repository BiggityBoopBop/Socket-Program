
/** The server receives radius of a circle from a client;
* calculate the area and send it back to the client.
* TCP protocol is used 
* @author Wenjuan Jiang
* @reference: "Java: how to program", tenth edition, Chapter 28.
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket server; // server socket
	private int serverPort = 12345; // server port
	private Socket connection;
	private DataInputStream input; // input stream from client
	private DataOutputStream output; // output stream to client
	private int counter = 1; // counter of number of connections

	public Server(int port) {
		this.serverPort = port;
	}
	// set up server to receive connections; process connections
	public void runServer() {
		
		try {
			server = new ServerSocket(serverPort, 100); // port: 12345, number of connections: 100
			while (true) {
				try {
					waitForConnection(); // wait for a connection
					getStreams();
					processConnection();
				} catch (EOFException eofException) {
					System.out.println("Server terminated connection");
				} finally {
					closeConnection(); // close connection
					++counter;
				}
			}
		} catch (IOException ioException) {
			// TODO Auto-generated catch block
			ioException.printStackTrace();
		}

	}

	// wait for connection to arrive and display connection info
	private void waitForConnection() throws IOException {
		System.out.println("Waiting for connection");
		connection = server.accept(); // allow server to accept connection
		System.out.println("Connection " + counter + " received from: " + connection.getInetAddress().getHostName());
	}

	// get streams to send and receive data
	private void getStreams() throws IOException {
		input = new DataInputStream(connection.getInputStream());
		output = new DataOutputStream(connection.getOutputStream());
		output.flush();
	}

	// process connection with client
	private void processConnection() throws IOException {
		String message = "connection successful\n";
		output.writeUTF(message);
		double radius;
		do {
			radius = input.readDouble();
			double area = radius * radius * Math.PI;
			output.writeDouble(area);
		} while (radius != -1);
	}

	// close streams and socket
	private void closeConnection() {
		System.out.println("Terminating connection");
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
