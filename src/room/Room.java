package room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import packets.Packet;
import packets.serverPackets.*;
import server.Server;

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
	private List<Player> players = Collections.synchronizedList( new ArrayList<Player>());
	private HashMap<Integer, Integer> playerIDMap = new HashMap<Integer, Integer>(); //Player ID -> Player Instance
	private int maxPlayerID = 0;
	
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
		addPlayer(hostName, MACAddress, clientID);
		//Create a Leaderboard
		
	}
	
	private void assignTargets(int clientID){
		switch (currentGame.getType()) {
		case Packet.GAMETYPE_DEFAULT:
			ArrayList<Player> validTargets = new ArrayList<Player>();
			int previousTarget = playerIDMap.get(players.get(playerIDMap.get(clientID)).getPreviousTarget());
			for(int i = 0; i < players.size(); i++){
				if(players.get(i).getTarget() != clientID && i != previousTarget && i != playerIDMap.get(clientID)){
					validTargets.add(players.get(i));
				}
			}
			if(validTargets.isEmpty()){
				NAKPacket np = new NAKPacket();
				np.setNAK(Packet.NAK_NO_VALID_TARGETS);
				Server.sendPacket(clientID, np);
				break;
			}
			Random rng = new Random();
			int target = rng.nextInt(validTargets.size());
			int targetID = validTargets.get(target).getID();
			players.get(playerIDMap.get(clientID)).setPlayerTarget(target);
			TargetPacket tp = new TargetPacket();
			tp.putTargetID(target);
			Server.sendPacket(clientID, tp);
			break;
		case Packet.GAMETYPE_TEAM:
			
			break;
		case Packet.GAMETYPE_MAN_HUNT:
			
			break;
		}
	}
	
	public void addPlayer(String playerName, double[] MACAddress, int clientID) {
		//for loop for checking if disconnected player has the same MAC as above, if so rejoin, else new player.
		players.add(new Player(playerName, MACAddress));
		playerIDMap.put(clientID, players.size());
		if(clientID > maxPlayerID){ maxPlayerID = clientID; }
	}
	
	public void quitPlayer(int clientID) {
		if (clientID == hostID) {
			//Set a new host
		}
		//players.get(playerIDMap.get(clientID)).setState("Disconnected");
	}
	
	public void setPlayerLocation(double[] location, int clientID) {
		players.get(clientID).setPlayerLocation(location);
	}
	
	public void setPlayerPing(int ping, int clientID) {
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