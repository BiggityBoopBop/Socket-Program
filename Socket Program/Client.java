
/** The client sends a radius of a circle to a server; The server
* calculates the area and send it back to the client.
* TCP protocol is used 
* @author Wenjuan Jiang
*/
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private int serverPort;
	private Socket client;
	private DataInputStream input; // input stream from client
	private DataOutputStream output; // output stream to client
	private String hostServer; // host server for this application
	private Scanner scan = new Scanner(System.in);

	public Client(String host) {
		hostServer = host;
		serverPort = 12345;
	}

	public Client(String host, int serverPort) {
		hostServer = host;
		this.serverPort = serverPort;
	}

	// connect to server and process messages from server
	public void runClient() {
		try {
			connectToServer();
			getStreams();
			processConnection();
		} catch (EOFException eofException) {
			System.out.println("Client terminated connection");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeConnection(); // close connection
		}
	}

	// connect to server
	private void connectToServer() throws IOException {
		System.out.println("Attempting connection");

		client = new Socket(InetAddress.getByName(hostServer), serverPort);
		// display connection info
		System.out.println("Connected to: " + client.getInetAddress().getHostName());

	}

	// get streams to send and receive data
	private void getStreams() throws IOException {
		input = new DataInputStream(client.getInputStream());
		output = new DataOutputStream(client.getOutputStream());
		output.flush();
	}

	// process connection with server
	private void processConnection() throws IOException {
		String message = input.readUTF();
		System.out.println(message);
		double radius;
		double area;

		do {
			System.out.println("Please input a value for radius: (-1 to exit)");
			radius = scan.nextDouble();
			output.writeDouble(radius);
			area = input.readDouble();
			System.out.println("The area received from the server is: " + area);
		} while (radius != -1);
	}

	// close stream and socket
	private void closeConnection() {
		System.out.println("Closing connection");
		try {
			output.close();
			input.close();
			client.close();
			scan.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
