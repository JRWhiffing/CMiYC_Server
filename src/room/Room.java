package room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import packets.Packet;
import packets.serverPackets.*;
import packets.serverPackets.broadcastPackets.*;
import server.Server;

public class Room {

	private int roomNumber;
	private String roomName;
	private Game currentGame;
	private enum State {
		GAME, LOBBY, STARTING, ENDING, PAUSED, FINISHED
	}
	private State roomState;
	private int voteCount;
	private int hostID;
	private Leaderboard leaderboard;
	private List<Player> players = Collections.synchronizedList( new ArrayList<Player>());
	private HashMap<Integer, Integer> playerIDMap = new HashMap<Integer, Integer>(); //Player ClientID -> Player Instance in players
	private int maxPlayerID = 0;
	private int roomSize;
	
	//Voting
	//Votes
	//etc.
	
	public Room(String roomName, int clientID, String hostName, double[] MACAddress) {
		this.roomName = roomName;
		this.hostID = clientID;
		currentGame = new Game((byte)0x00, 0, 0, 0, new double[] {0.0,0.0}, 0, 0);
		addPlayer(hostName, MACAddress, clientID);
		leaderboard = new Leaderboard();
		leaderboard.addPlayer(clientID, hostName);	
		roomState = State.LOBBY;
	}
	
	public void changeGameType(byte gameType) {
		currentGame.changeGameType(gameType);
	}
	
	private void assignTargets(int clientID){
		//need to weight higher scoring players as more likely targets, also only a max of 3 pursuers.
		switch (currentGame.getType()) {
		case Packet.GAMETYPE_DEFAULT:
			ArrayList<Player> validTargets = new ArrayList<Player>();
			int previousTarget = playerIDMap.get(players.get(playerIDMap.get(clientID)).getPreviousTarget());
			for(int i = 0; i < players.size(); i++){
				if(players.get(i).getTarget() != clientID && i != previousTarget && i != playerIDMap.get(clientID)
				   && players.get(i).getState().equals("CONNECTED")){
					if((players.size() > 4 && players.get(i).getPursuerCount() < 3) || 
					   (players.size() == 4 && players.get(i).getPursuerCount() < 2) || 
					   (players.size() == 3 && players.get(i).getPursuerCount() < 1)){
						validTargets.add(players.get(i));
					}
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
			players.get(playerIDMap.get(clientID)).setPlayerTarget(targetID);
			players.get(playerIDMap.get(targetID)).addPursuer();
			TargetPacket tp = new TargetPacket();
			tp.putTargetID(new int[]{targetID});
			Server.sendPacket(clientID, tp);
			break;
			
		case Packet.GAMETYPE_TEAM:
			//send IDs of other team;
			
			break;
		case Packet.GAMETYPE_MAN_HUNT:
			//send IDs of other team;
			
			break;
		}
	}
	
	private void assignTeams(){
		int team1 = roomSize / 2;
		int team2 = roomSize - team1;
		Random rng = new Random();
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getState().equals("CONNECTED")){
				if(team1 == 0){
					players.get(i).setTeam(2);
					leaderboard.updateTeam(players.get(i).getID(), 2);
					team2--;
				} else if(team2 == 0){
					players.get(i).setTeam(1);
					leaderboard.updateTeam(players.get(i).getID(), 1);
					team1--;
				} else{
					int team = rng.nextInt(2);
					if(team == 1){
						players.get(i).setTeam(1);
						leaderboard.updateTeam(players.get(i).getID(), 1);
						team1--;
					} else {
						players.get(i).setTeam(2);
						leaderboard.updateTeam(players.get(i).getID(), 2);
						team2--;
					}
				}
			}
		}
	}
	
	private void removeTeams(){
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getState().equals("CONNECTED")){
				leaderboard.updateTeam(players.get(i).getID(), 0);
			}
		}
	}
	
	private void broadcast(Packet broadcastPacket){
		for(int i = 0; i < players.size(); i++){
			if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")){
				Server.sendPacket(players.get(i).getID(), broadcastPacket);
			}
		}
	}
	
	private void startGame(){
		
	}
	
	public void addPlayer(String playerName, double[] MACAddress, int clientID) {
		//need to broadcast relevant leaderboard and player joined
		if(roomSize == 16){
			NAKPacket np = new NAKPacket();
			np.setNAK(Packet.NAK_ROOM_FULL);
			Server.sendPacket(clientID, np);
			return;
		}
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getState().equals("DISCONNECTED") && players.get(i).getMACAddress().equals(MACAddress)){
				players.get(i).setID(clientID);
				playerIDMap.put(clientID, i);
				players.get(i).rejoined();
				return;
			}
		}
		players.add(new Player(playerName, MACAddress));
		playerIDMap.put(clientID, players.size());
		leaderboard.addPlayer(clientID, playerName);
		if(clientID > maxPlayerID){ maxPlayerID = clientID; }
		roomSize++;
	}
	
	public void quitPlayer(int clientID) {
		roomSize--;
		players.get(playerIDMap.get(clientID)).removePlayer();
		leaderboard.removePlayer(clientID);
		if (clientID == hostID) {
			setNewHost();			
		}
	}
	
	public void changeHost(int hostID) {
		//Changes the host to the new ID
		this.hostID = hostID;
	}
	
	public void setNewHost() {
		ArrayList<Integer> playersAvailable = new ArrayList<Integer>();
		for(int i = 0; i < players.size(); i++){
			if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")){
				playersAvailable.add(players.get(i).getID());
			}
		}
		Random rng = new Random();
		int numberOfPlayersInGame = playersAvailable.size(); //Number of players in game
		int newHost = rng.nextInt(numberOfPlayersInGame);
		hostID = playersAvailable.get(newHost); //Use this integer number in the arraylist to get the player in that position
		Server.sendPacket(hostID, new HostPacket()); //Alert client that they are the new host
		NewHostPacket nhp = new NewHostPacket();
		nhp.putPlayerID(hostID);
		broadcast(nhp); //Tell all clients there is a new host
	}
	
	public void setPlayerLocation(double[] location, int clientID) {
		players.get(playerIDMap.get(clientID)).setPlayerLocation(location);
	}
	
	public void setPlayerPing(int ping, int clientID) {
		players.get(playerIDMap.get(clientID)).setPlayerPing(ping);
	}
	
	public void toggleVoting() {
		currentGame.allowVoting();
	}
	
	public void catchPerformed(int clientID) {
		int targetID = players.get(playerIDMap.get(clientID)).getTarget(); // Target of player performing catch
		if (checkCaptured(targetID)) {
			players.get(playerIDMap.get(targetID)).beenCaught(); //Changes the state of the player to changing
			leaderboard.updatePlayerScore(clientID, 100);
			//SEND LEADERBOARD PACKETS TO ALL CLIENTS
			//Change the Target
		}
		else {
			//Something needs to happen if response didn't come in
		}
	}
	
	public boolean checkCaptured(int targetID) {
		long currentTime = System.currentTimeMillis();
		long maxTime = currentTime + 10000; //Checks for 10 seconds
		while(System.currentTimeMillis() < maxTime) {
			if (players.get(playerIDMap.get(targetID)).checkCaught()) {
				return true;
			}
		}
		return false;
		//Needs to see if captured has been called by the targetID
	}
	
	
	///Player presses caught button but pursuer hasn't pressed button yet. - This may need changing if the caught button only appears when pursuer presses button
	public void captured(int clientID) {
		players.get(playerIDMap.get(clientID)).captured();
		long currentTime = System.currentTimeMillis();
		long maxTime = currentTime + 10000; //Checks for 10 seconds
		while(System.currentTimeMillis() < maxTime) {
			if (players.get(playerIDMap.get(clientID)).checkContinue()) {
				//Change the pursuer
			}
		}
		//Something needs to happen if player has not been caught
	}
	
	public void setTimeLimit(int time) {
		currentGame.setTimeLimit(time);
	}
	
	public void setBoundaryLimit(double[] boundaries, int radius) {
		currentGame.setBoundariesCentre(boundaries);
		currentGame.setBoundariesRadius(radius);
	}
	
	public void setBoundariesUpdates(int[] updates) {
		currentGame.setBoundariesUpdateInterval(updates[0]);
		currentGame.setBoundariesUpdatePercentage(updates[1]);
	}
	
	public void setScoreLimit(int score) {
		currentGame.setScoreLimit(score);
	}
	
	public void endGame() {
		//Functionality to end the game
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
	
	public int size(){
		return roomSize;
	}
	
}