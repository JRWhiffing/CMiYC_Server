package room;

import java.util.HashMap;

public class Room {

	private int roomNumber;
	private String roomName;
	private Game currentGame;
	private String roomKey; //String, Int? 
	//		I'd suggest a 5 digit 36 base (alphabet + 0123456789) number which will be stored as a string
	private enum State {
		GAME, LOBBY, STARTING, ENDING, PAUSED, FINISHED
	}
	private int voteCount;
	private int hostID;
	private double[] hostMACAddress;
	private Leaderboard leaderboard;
	private HashMap<Integer, Integer> targets = new HashMap<Integer, Integer>();
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	
	
	//Players
	//Game (Object?)
	//Name
	//Key
	//State? i.e. Game , Lobby , Starting , Ending, etc.
	//Host?
	//Voting
	//Votes
	//etc.
	
	public Room(String roomName, int clientID, String hostName, double[] MACAddress) {
		this.roomName = roomName;	
		players.put(clientID, new Player(hostName, MACAddress));
	}
	
	public void addPlayer(String playerName, double[] MACAddress, int clientID) {
		players.put(clientID, new Player(playerName, MACAddress));
	}
	
	public int getRoomNumber(){
		return roomNumber;
	}


	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public Game getCurrentGame() {
		return currentGame;
	}


	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}


	public String getRoomKey() {
		return roomKey;
	}


	public void setRoomKey(String roomKey) {
		this.roomKey = roomKey;
	}


	public double getHostID() {
		return hostID;
	}

}