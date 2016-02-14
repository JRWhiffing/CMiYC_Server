package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	private ServerSocket listener;
	private Thread serverListener;
	private int portNumber = 0;
	
	public static void main(String[] args) {
		Server server = new Server();
	}
	
	Server(){
		createListener();
		serverListener = new Thread(){
			public void run(){
				try {
					//listening to the socket for clients.
					while(!Thread.currentThread().isInterrupted()){
						Socket clientSocket = null;
						System.out.println("Server: Waiting for a client on the port: "+portNumber);
						clientSocket = listener.accept();
						System.out.println("Server: Client found, passing to ClientHandler.");
						/*
						 * Do we want to have the player class to also be the client handler
						 * or would they be separate, i.e. player data in separate object to the
						 * socket and thread?
						 */
					}
				} catch (IOException ioe) {
					System.err.println("Server: there was an error connecting to the client. "+ioe.getMessage());
				} finally {
					//closing the socket and thread.
					try {
						if(listener != null){
							listener.close();
						}
						System.out.println("Closing the listener");
						
						Thread.currentThread().interrupt();
						} catch (IOException ioe) {
							System.err.println("Server: There was an error closing the socket. "+ioe.getMessage());
						}
					}
				}
			};
			serverListener.start();
	}
	
	private void createListener(){
		boolean found = false;
		int portNum = 1234;
		InetAddress addr = null;
		
		try {
			addr = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("InetAddress unresolved for local host");
			System.exit(1);
		}
		
		while(!found){
			try {
				listener = setListener(portNum, addr);
				found = true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.err.println("Server: Port "+portNum+" unavailable, attempting new port "+(portNum+1));
				found = false;
			}
		}
		portNumber = portNum;
	}
	
	private ServerSocket setListener(int portNum, InetAddress addr) throws Exception{
		ServerSocket temp = null;
		try {
			temp = new ServerSocket(portNum, 0, addr);
		} catch (IOException ioe) {
			throw new Exception("Failed to secure socket: " + ioe.getMessage());
		}
		return temp;
	}
}
