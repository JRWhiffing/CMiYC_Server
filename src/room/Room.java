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
	
	public void quitPlayer(int clientID) {
		if (clientID == hostID) {
			//Set a new host
		}
		else {
			players.get(clientID).removePlayer();
		}
		//Needs to save the data of previous player incase they come back?
		//What if the Player is Host - Needs to Set a New host
	}
	
	public void setPlayerLocation(double[] location, int clientID) {
		players.get(clientID).setPlayerLocation(location);
	}
	
	public void setPlayerPing(double ping, int clientID) {
		players.get(clientID).setPlayerPing(ping);
	}
	
	public void catchPerformed(int clientID) {
		//Check that this is correct
		//Update ScoreBoard, Change Target HashMap
	}
	
	public void captured(int clientID) {
		//Check that this is correct
		//Change Pursuer?
	}
	
	public void abilityUsage(byte ability, int clientID) {
		//players.get(clientID).abilityUsage(ability);
		//Player uses ability
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

	public void setHostID(int hostID) {
		this.hostID = hostID;
	}
	
}