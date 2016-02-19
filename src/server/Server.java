package server;

import java.util.HashMap;
import java.util.Random;

import server.room.*;

public class Server {	
	
	private static boolean active; 
	private static final HashMap<String,Room> ROOMS = new HashMap<String, Room>(); //Have max limit?
	//to be used to view all rooms without knowing keys by storing key with room number, i.e. view active rooms.
	private static final HashMap<Integer, String> roomKeys = new HashMap<Integer, String>();
	private static int lastRoom = 0;
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
	
	private static String generateRoomKey(){
		Random random = new Random();
		int char_;
		String key = "";
		boolean valid = false;
		while (!valid){
			for(int i = 0; i < 5; i++){
				char_ = random.nextInt(36);
				key += Integer.toString(char_, 36);
			}
			if(ROOMS.containsKey(key)){
				key = "";
			} else {
				valid = true;
			}
		}
		
		return key;
	}
	
	public static synchronized void createRoom(String roomName, int clientID, String clientName /*MAC ADDRESS??*/){
		String key = generateRoomKey();
		//ROOMS.put(key, new Room(roomName));
		//ROOMS.get(key).addPlayer(clientID, clientName, MAC Address, avatar);
		
	}
	
}