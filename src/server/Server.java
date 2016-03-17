package server;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
		if(roomKey != null){
			//Server.ROOMS.get(roomkey).removePlayer(client);
		}
		serverListener.closeClient(client);
	}
	
	public synchronized static void sendPacket(int clientID, Packet serverPacket){
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
	
	public synchronized static void playerJoin(String roomKey, double[] MACAddress, String playerName, int clientID) {
		Server.ROOMS.get(roomKey).addPlayer(playerName, MACAddress, clientID);
	}
	
	public synchronized static void setLocation(String roomKey, int clientID, double[] location) {
		Server.ROOMS.get(roomKey).setPlayerLocation(location, clientID);
	}
	
	public synchronized static void pingResponse(String roomKey,int clientID, int ping) {
		Server.ROOMS.get(roomKey).setPlayerPing(ping, clientID);
	}
	
	public synchronized static void catchPerformed(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).catchPerformed(clientID);
	}
	
	public synchronized static void captured(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).captured(clientID);
	}
	
	public synchronized static void abilityUsage(String roomKey, int clientID, byte ability) {
		Server.ROOMS.get(roomKey).abilityUsage(ability, clientID);
	}
	
	public synchronized static void vote(String roomKey, int clientID, byte vote) {
		//Voting -> Game not Room? Server.ROOMS.get(roomKey).voteRoom(vote, clientID);
		//Assumed the voting would only be used for choosing a gamemode
	}
	
	public synchronized static void playerReported(String roomKey,int report, int clientID) {
		//Does something with reporting - Don't know what this is
		//if a player gets above a threshold of reports then they are kicked.
		//the threshold may be dynamic, i.e. 33-50% of players have voted to kick the player
	}
	
	public synchronized static void kickPlayer(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).quitPlayer(clientID); //Should do the same as when the player quits? Unless we dont want to keep a record of their data
	}
	
	public synchronized static void playerQuit(String roomKey, int clientID) {
		Server.ROOMS.get(roomKey).quitPlayer(clientID);
	}
	
	public synchronized static void setTimeLimit(String roomKey, int time) {
		Server.ROOMS.get(roomKey).setTimeLimit(time);
	}
	
	public synchronized static void setBoundariesUpdates(String roomKey, int[] updates) {
		Server.ROOMS.get(roomKey).setBoundariesUpdates(updates);
	}
	
	public synchronized static void setBoundaries(String roomKey, double[] boundaries, int radius) {
		Server.ROOMS.get(roomKey).setBoundaryLimit(boundaries, radius);
	}
	
	public synchronized static void setScoreLimit(String roomKey, int score) {
		Server.ROOMS.get(roomKey).setScoreLimit(score);
	}
	
	public synchronized static void toggleVoting(String roomKey) {
		Server.ROOMS.get(roomKey).toggleVoting();
	}
	
	public synchronized static void changeHost(String roomKey, int clientID) {
		//The ID is the new hosts ID not the current hosts
		Server.ROOMS.get(roomKey).changeHost(clientID);
	}
	
	public synchronized static void changeGameType(String roomKey, byte gameType) {
		Server.ROOMS.get(roomKey).changeGameType(gameType);
	}
	
	public synchronized static void endGame(String roomKey) {
		Server.ROOMS.get(roomKey).endGame();
	}
	
	public synchronized static void startGame(String roomKey) {
		//Server.ROOMS.get(roomKey).startGame();
	}
	
	public synchronized static void closeRoom(String key) {
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
	
	public synchronized static void createRoom(int clientID, String roomName, String clientName, double[] MACAddress){
		String key = generateRoomKey();
		Server.ROOMS.put(key, new Room(roomName, clientID, clientName, MACAddress));
		Server.ROOMKEYS.put(lastRoom, key);
		lastRoom++;
		RoomKeyPacket rkp = new RoomKeyPacket();
		rkp.putRoomKey(key);
		Server.sendPacket(clientID, rkp);
		serverListener.setRoomKey(clientID, key);
	}
	
	
	///TIMER IMPLEMENTATION - DOES NOT WORK BECAUSE ENDGAME HAS TO BE STATIC
	/**
	 * Creates and starts a timer based on the room and the time limit
	 * Should be called when the game is started
	 * @param key The Room Key
	 * @param time The time limit of the game
	 */
	public static void startTimer(String key, int time) {
		Timer gameTimer = new Timer();
		gameTimer.schedule(new EndGame(key), (long)time);
	}
	
	/**
	 * TimerTask which calls the run function when the timer reaches its time
	 *
	 */
	 static class EndGame extends TimerTask {
		String roomKey;
		
		public EndGame(String roomKey) {
			this.roomKey = roomKey;
		}
		
		public void run() {
			Server.ROOMS.get(roomKey).endGame();
		}
	}
	
//	public static void printRooms(){
//		
//	}
	
}