package server;

import java.util.HashMap;

import server.room.*;

public class Server {	
	
	private static boolean active; 
	private static final HashMap<String,Room> Rooms = new HashMap<String, Room>();; //Have max limit?
	private static Thread serverListener;
	
	public static void main(String[] args) {
		serverListener = new Thread( new ServerListener());
		serverListener.run();//creating a Thread in which to listen for new clients.
		
		active = true;
		while(active){ /*read from command line for input?*/ }//loop forever until Server closed. (need to do anything here?
		
		System.exit(0);
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
	
	public static void closeServer(){
		((ServerListener) serverListener).closeConnections();
		try {
			Thread.currentThread().wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!serverListener.isInterrupted()){
			serverListener.interrupt();
		}
		active = false;
	}
	
}