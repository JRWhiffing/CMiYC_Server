package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import packets.Packet;
import packets.serverPackets.RoomKeyPacket;
import room.*;

/**
 * Class for Server Functionality
 * Multiple clients can access the Server and so methods needs to be synchronized
 * A room hashmap stores all the rooms in the server
 * @authors James and Adam
 *
 */
public class Server {	
	
	private static boolean active; 
	private static final HashMap<String,Room> ROOMS = new HashMap<String, Room>(); //Have max limit?
	//to be used to view all rooms without knowing keys by storing key with room number, i.e. view active rooms.
	private static final HashMap<Integer, String> ROOMKEYS = new HashMap<Integer, String>();
	private static int lastRoom = 0;
	private static ServerListener serverListener;
	
	/**
	 * Main Method for starting the server. Creates and starts up the Server Listener
	 * Once server has been created then waits for input
	 * @param args - Input arguments
	 */
	public static void main(String[] args) {
		serverListener = new ServerListener();
		serverListener.start(); //Creates a Thread to listen for new clients.
		active = true; //Server is active
		mainloop:
		while (active) {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			try {
				String line = "";
				while ((line = inputReader.readLine()) != null) {
					line = line.toUpperCase();
					switch (line) {
					
					//Shows all the room states and number of active players in each room
					case "SHOW ROOMS" :
						if (!ROOMS.isEmpty()) {
							
							for(int i = 0; i < lastRoom; i++) {
								System.out.println("ROOM " + i + ROOMS.get(ROOMKEYS.get(i)).getRoomName() + " with RoomKey " + 
								ROOMKEYS.get(i) + " is in State: " + ROOMS.get(ROOMKEYS.get(i)).getRoomState());
								System.out.println("ROOM " + i + " has " + ROOMS.get(ROOMKEYS.get(i)).getPlayerCount() + " active players" );
								System.out.println(ROOMS.get(ROOMKEYS.get(i)).getLeaderboard().toString());
							}
						}
						else {
							System.out.println("No Rooms have been created");
						}
						break;
					
					//Shuts down the Server
					case "EXIT" :
						break mainloop;
						
					//For unrecognised input	
					default :
						System.out.println("Unrecognised Input");
						break;
							
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Server is shutting down...");
		
		//Loops all rooms and closes them 
		for (int i = 0; i < lastRoom; i++) {
			String roomKey = Server.ROOMKEYS.get(i);
			if (Server.ROOMS.get(roomKey).getRoomState() != "FINISHED") {
				closeRoom(roomKey);
			}
		}
		//Closes all connections with clients
		closeServer();
		
		System.out.println("Server has Successfully shut down");
		
		System.exit(0);
	}
	
	/**
	 * Method for sending a packet to a client
	 * @param clientID - Integer ID of the Client
	 * @param serverPacket - Packet that needs to be sent to client
	 */
	public synchronized static void sendPacket(int clientID, Packet serverPacket) {
		serverListener.sendPacket(clientID, serverPacket);
	}
	
	/**
	 * Method to return the room object
	 * @param roomKey - The room key of the room
	 */
	public static Room getRoom(String roomKey){
		return Server.ROOMS.get(roomKey);
	}
	
	/**
	 * Method for closing the server
	 */
	public static void closeServer() {
		serverListener.closeConnections();
		active = false; //Server is not active
	}
	
	/**
	 * Method for creating a new Room Key
	 * @return - Returns the new Room Key as a String
	 */
	private synchronized static String generateRoomKey() {
		Random random = new Random();
		int char_;
		String key = "";
		boolean valid = false;
		while (!valid) {
			//Generates a 5 character random room key
			for(int i = 0; i < 5; i++) {
				char_ = random.nextInt(36);
				key += Integer.toString(char_, 36);
			}
			//Checks if there is a room key already with that string
			if(Server.ROOMS.containsKey(key)) {
				key = "";
			} else {
				valid = true; //If not then exit while loop
			}
		}
		return key;
	}
	
	/**
	 * Method that creates a Room instance and adds the first player into it
	 * @param clientID - The integer ID of the First Player
	 * @param roomName - The name of the Room
	 * @param clientName - The name of the First Player
	 * @param MACAddress - The MAC Address of the First Player
	 */
	public synchronized static void createRoom(int clientID, String roomName, String clientName, double[] MACAddress){
		String key = generateRoomKey();
		Server.ROOMS.put(key, new Room(roomName, clientID, clientName, MACAddress, key));
		Server.ROOMKEYS.put(lastRoom, key);
		lastRoom++;
		RoomKeyPacket rkp = new RoomKeyPacket();
		rkp.putRoomKey(key);
		Server.sendPacket(clientID, rkp);
		serverListener.setRoomKey(clientID, key);
	}
	/**
	 * Method that closes the room
	 * @param key - The room key of the room as a String
	 */
	public synchronized static void closeRoom(String roomKey) {
		Server.ROOMS.get(roomKey).endGame();
		//Waits for the Game the End
		while(Server.ROOMS.get(roomKey).getRoomState() != "FINISHED") { }
		Server.ROOMS.remove(roomKey);
		for(int i = 0; i < lastRoom; i++){
			if(Server.ROOMKEYS.containsKey(i)){
				if(Server.ROOMKEYS.get(i).equals(roomKey)){
					Server.ROOMKEYS.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Method that starts a game
	 * @param roomKey - The room key of the room
	 */
	public synchronized static void startGame(String roomKey) {
		Server.ROOMS.get(roomKey).startGame(Server.ROOMS.get(roomKey));
	}
	
	/**
	 * Method that ends a game
	 * @param roomKey - The room key of the room
	 */
	public synchronized static void endGame(String roomKey) {
		Server.ROOMS.get(roomKey).endGame();
	}
	
	/**
	 * Method for when a new Player has joined
	 * @param roomKey - The room key of the room
	 * @param MACAddress - The MAC Address of the Player that has joined
	 * @param playerName - The name of the Player that has joined as a String
	 * @param clientID - The integer ID of the Player that has joined
	 */
	public synchronized static void playerJoin(String roomKey, double[] MACAddress, String playerName, int clientID) {
		System.out.println("adding player (Server)");
		Server.ROOMS.get(roomKey).addPlayer(playerName, MACAddress, clientID);
	}
	
	/**
	 * Method for setting the location of a Player
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the Player
	 * @param location - The location coordinates as an array
	 */
	public synchronized static void setLocation(String roomKey, int clientID, double[] location) {
		Server.ROOMS.get(roomKey).setPlayerLocation(location, clientID);
	}
	
	/**
	 * Method for interrupting the player timer to determine the ping
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the Player
	 */
	public synchronized static void pingResponse(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).interruptPlayerTimer(clientID);;
	}
	
	/**
	 * Method for informing the room that a Player has performed a capture
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the pursuer
	 */
	public synchronized static void catchPerformed(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).catchPerformed(clientID);
	}
	
	/**
	 * Method for informing the room that a Player has been captured
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the caught player
	 */
	public synchronized static void captured(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).captured(clientID);
	}
	
	/**
	 * Method for using an ability
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the player
	 * @param ability - The ability ID of the ability that the player is using
	 */
	public synchronized static void abilityUsage(String roomKey, int clientID, byte ability) {
		Server.ROOMS.get(roomKey).abilityUsage(ability, clientID);
	}
	
	/**
	 * Method calls the Room Class' voteGameMode method
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the player that has cast the vote
	 * @param vote - The vote that was cast by the player
	 */
	public synchronized static void vote(String roomKey, int clientID, byte vote) {
		Server.ROOMS.get(roomKey).voteGameMode(vote, clientID);
	}
	
	/**
	 * Method calls the Room Class' report method
	 * @param roomKey - The room key of the room
	 * @param reportedID - The integer ID of the reported player
	 * @param clientID - The integer ID of the player reporting the reported player
	 */
	public synchronized static void playerReported(String roomKey, int reportedID, int clientID) {
		Server.ROOMS.get(roomKey).report(reportedID, clientID);
	}
	
	/**
	 * Method that kicks a player from a game
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the kicked player
	 */
	public synchronized static void kickPlayer(String roomKey, int clientID) {
		//Currently Kick does the same as Quit - May want to change so that we keep the player's data
		Server.ROOMS.get(roomKey).quitPlayer(clientID, Packet.DISCONNECT_KICK);
		serverListener.closeClient(clientID);
	}
	
	public synchronized static void disconnectPlayer(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).quitPlayer(clientID, Packet.DISCONNECT_POOR_CONNECTION);
		serverListener.closeClient(clientID);
	}
	
	/**
	 * Method that quits a player from a game
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the player that is quitting
	 */
	public synchronized static void quitPlayer(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).quitPlayer(clientID, Packet.DISCONNECT_QUIT);
		serverListener.closeClient(clientID);
	}
	
	/**
	 * Method that sets the time limit of a game
	 * @param roomKey - The room key of the room
	 * @param time - The integer time limit
	 */
	public synchronized static void setTimeLimit(String roomKey, int time) {
		Server.ROOMS.get(roomKey).setTimeLimit(time);
	}
	
	/**
	 * Method that sets the boundary update time
	 * @param roomKey - The room key of the room
	 * @param updates - An array of integers where the first element is the time interval and the second element is the percentage decrease
	 */
	public synchronized static void setBoundariesUpdates(String roomKey, int[] updates) {
		Server.ROOMS.get(roomKey).setBoundariesUpdates(updates);
	}
	
	/**
	 * Method that sets the boundaries of a game
	 * @param roomKey - The room key of the room
	 * @param boundaries - The centre coordinates of boundaries as an array
	 * @param radius - The integer radius of the boundary circle
	 */
	public synchronized static void setBoundaries(String roomKey, double[] boundaries, int radius) {
		Server.ROOMS.get(roomKey).setBoundaryLimit(boundaries, radius);
	}
	
	/**
	 * Method that sets the score limit of a game
	 * @param roomKey - The room key of the room
	 * @param score - The integer score limit of the game
	 */
	public synchronized static void setScoreLimit(String roomKey, int score) {
		Server.ROOMS.get(roomKey).setScoreLimit(score);
	}
	
	/**
	 * Method that toggles voting features in a game
	 * @param roomKey - The room key of the room
	 */
	public synchronized static void toggleVoting(String roomKey) {
		Server.ROOMS.get(roomKey).toggleVoting();
	}
	
	/**
	 * Method that sets acknowledgement of a player in a room
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the acknowledging player
	 */
	public synchronized static void acknowledgement(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).acknowledgePlayer(clientID);
	}
	
	/**
	 * Method that changes the host of a game
	 * @param roomKey - The room key of the room
	 * @param clientID - The integer ID of the new host
	 */
	public synchronized static void changeHost(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).changeHost(clientID);
	}
	
	/**
	 * Method that changes the game type of a game
	 * @param roomKey - The room key of the room
	 * @param gameType - The byte ID of the new game type
	 */
	public synchronized static void changeGameType(String roomKey, byte gameType) {
		Server.ROOMS.get(roomKey).changeGameType(gameType);
	}
	
	public synchronized static void setRoomKey(int clientID, String key){
		Server.serverListener.setRoomKey(clientID, key);
	}
	
}
