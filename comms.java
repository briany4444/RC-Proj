import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

    
public class comms {

    // Communication
	private int port;
	private InetAddress IPAddress;
	private DatagramSocket clientSocket;
    

	public comms() {
		setupComms();
	}

    /**
	 * Setup code that creates all the links to the server. Do not call outside of constructors.
	 */
	private void setupComms(){
		//  Send first the init command to let server know a new player is connecting
		try {
			IPAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		// Setup socket
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		byte[] sendData;
		byte[] receiveData = new byte[1024];


		// if (gk) {
		// 	sendData = ("(init " + teamName + " (version 15) (goalie)) ").getBytes();
		// } else {
		sendData = ("(init " + "bro" + " (version 15)) ").getBytes();
		System.out.println(IPAddress);
		
		System.out.println("bruh");

		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6000);
		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// after sending the init command to the server,
		// specifying the team, the player will receive its own port to communicate
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			clientSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}


		IPAddress = receivePacket.getAddress(); // new IP address unique to this player
		port = receivePacket.getPort(); // new port unique to this player


		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("First packet received : " + modifiedSentence); // receiving the confirmation with side and uniformnumber

		// receiving the rest of the data, server_param, player_param, player_type
		for (int i = 0; i < 20; i++) {
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			modifiedSentence = new String(receivePacket.getData());
		}

		System.out.println(modifiedSentence);
    }

    public static void main(String[] args) {
		comms c = new comms();
    }

}
