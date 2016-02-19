package server;

import java.util.HashMap;

import server.room.*;

public class Server {	
	
	private static boolean active; 
	private static final HashMap<String,Room> Rooms = new HashMap<String, Room>();; //Have max limit?
	private static ServerListener serverListener;
	
	public static void main(String[] args) {
		serverListener = new ServerListener();
		serverListener.run();//creating a Thread in which to listen for new clients.
		
		active = true;
		//loop forever until Server closed. (need to do anything here?)
		while(active){
			/*read from command line for input?*/ 
		}
		
		//code for closing down rooms and server
		
		System.exit(0);
	}
	
	public static void sendPacket(int clientID, byte[] sp){
		System.out.println("Still Returning");
		serverListener.sendPacket(clientID, sp);
	}
	
	public static void closeServer(){
		serverListener.closeConnections();
		active = false;
	}
	
}