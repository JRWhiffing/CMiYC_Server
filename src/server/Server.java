package server;

import java.util.HashMap;
import java.util.Random;

import packets.Packet;
import packets.serverPackets.RoomKeyPacket;
import room.*;

public class Server {	
	
	private static boolean active; 
	private static final HashMap<String,Room> ROOMS = new HashMap<String, Room>(); //Have max limit?
	//to be used to view all rooms without knowing keys by storing key with room number, i.e. view active rooms.
	private static final HashMap<Integer, String> ROOMKEYS = new HashMap<Integer, String>();
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
	
	public static void closeClient(String roomKey, int client){
		//Server.ROOMS.get(roomkey).removePlayer(client);
		serverListener.closeClient(client);
	}
	
	public static void sendPacket(int clientID, Packet serverPacket){
		System.out.println("Still Returning");
		serverListener.sendPacket(clientID, serverPacket);
	}
	
	public static void closeServer(){
		serverListener.closeConnections();
		active = false;
	}
	
	private synchronized static String generateRoomKey(){
		Random random = new Random();
		int char_;
		String key = "";
		boolean valid = false;
		while (!valid){
			for(int i = 0; i < 5; i++){
				char_ = random.nextInt(36);
				key += Integer.toString(char_, 36);
			}
			if(Server.ROOMS.containsKey(key)){
				key = "";
			} else {
				valid = true;
			}
		}
		
		return key;
	}
	

	public static void playerJoin(String roomKey, double[] MACAddress, String playerName, int clientID) {
		Server.ROOMS.get(roomKey).addPlayer(playerName, MACAddress, clientID);
	}
	
	public static void setLocation(String roomKey, int clientID, double[] location) {
		Server.ROOMS.get(roomKey).setPlayerLocation(location, clientID);
	}
	
	public static synchronized void closeRoom(String key){
		//Server.ROOMS.get(key).close();
		//while(Server.ROOMS.get(key).getState() != "Finished"){ }
		Server.ROOMS.remove(key);
		for(int i = 0; i < lastRoom; i++){
			if(Server.ROOMKEYS.containsKey(i)){
				if(Server.ROOMKEYS.get(i).equals(key)){
					Server.ROOMKEYS.remove(i);
					break;
				}
			}
		}
	}
	
	public static synchronized void createRoom(int clientID, String roomName, String clientName, double[] MACAddress){
		String key = generateRoomKey();
		Server.ROOMS.put(key, new Room(roomName, clientID, clientName, MACAddress));
		Server.ROOMKEYS.put(lastRoom, key);
		lastRoom++;
		RoomKeyPacket rkp = new RoomKeyPacket();
		rkp.putRoomKey(key);
		Server.sendPacket(clientID, rkp);
		serverListener.setRoomKey(clientID, key);
	}
	
	public static void printRooms(){
		
	}
	
}