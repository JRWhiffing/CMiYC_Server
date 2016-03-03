package room;

import java.util.HashMap;

public class Room {

	private int roomNumber;
	private String roomName;
	private Game currentGame;
	private enum State {
		GAME, LOBBY, STARTING, ENDING, PAUSED, FINISHED
	}
	private int voteCount;
	private int hostID;
	private Leaderboard leaderboard;
	private HashMap<Integer, Integer> targets = new HashMap<Integer, Integer>(); //Player ID -> Player's Target ID
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>(); //Player ID -> Player Instance
	
	
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
		this.hostID = clientID;
		players.put(clientID, new Player(hostName, MACAddress));
		//Create a Leaderboard
		
	}
	
	public void addPlayer(String playerName, double[] MACAddress, int clientID) {
		players.put(clientID, new Player(playerName, MACAddress));
	}
	
	public void setPlayerLocation(double[] location,int clientID) {
		players.get(clientID).setPlayerLocation(location);
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
	
	public double getHostID() {
		return hostID;
	}

<<<<<<< HEAD
	public void setHostID(double hostID) {
		this.hostID = hostID;
	}
	
=======
>>>>>>> 0770fe77b9d2a1b75bf3c504b509cc7eac0b74ed
}