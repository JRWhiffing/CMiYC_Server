package server;

import java.util.HashMap;

import server.room.*;

public class Server {	
	
	private static boolean active; 
	private final static HashMap<String,Room> Rooms = new HashMap<String, Room>();; //Have max limit?
	
	public static void main(String[] args) {
		ServerListener serverListener = new ServerListener();
		serverListener.run();//creating a Thread in which to listen for new clients.
		
		active = true;
		while(active){ /*read from command line for input?*/ }//loop forever until Server closed. (need to do anything here?
		
		//code for closing down rooms and server
		
	}
	
	//returning a room for the StateMachine to be able to perform actions within Rooms.
	public static Room getRoom(String roomKey){
		return Rooms.get(roomKey);
	}
	
	//Allowing the StateMachine to add Clients to a room, provided a valid room key is given.
	public static boolean addClientToRoom(String roomKey /*, String nickName, MAC Address (not sure what data type)*/){
		if(Rooms.containsKey(roomKey)){
			//activeRooms.get(roomKey).addClient/Player(nickName, MAC Address);
			return true;
		}
		return false;
	}
	
	
	
}
