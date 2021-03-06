package room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import packets.LocationPacket;
import packets.Packet;
import packets.serverPackets.*;
import packets.serverPackets.broadcastPackets.*;
import packets.serverPackets.lobbyInfoPackets.BoundariesPacket;
import packets.serverPackets.lobbyInfoPackets.GametypePacket;
import packets.serverPackets.lobbyInfoPackets.ScoreLimitPacket;
import packets.serverPackets.lobbyInfoPackets.TimeLimitPacket;
import packets.serverPackets.lobbyInfoPackets.VotesPacket;
import server.Server;

/**
 * Class for room functionality
 * Each game will have its own Room instance, this is used to look after the game instance and all the player instances
 * @authors James and Adam
 *
 */
public class Room {

	private final String roomKey; //Is this needed?
	private String roomName;
	private Game currentGame;
	private int roomSize;
	
	private enum State {
		GAME, LOBBY, STARTING, ENDING, PAUSED, FINISHED
	}
	private State roomState;

	private Leaderboard lobbyLeaderboard;
	private List<Player> players;
	private HashMap<Integer, Integer> playerIDMap; //Player ClientID -> Player Instance in players
	private Timer gameTimer;
	private int maxPlayerID;
	private int hostID;
	private boolean votesEnabled = true;
	private Timer lt;
	
	/**
	 * Constructor that sets up all the global variables and creates a new game instance and a new lobbyLeaderboard
	 * @param roomName - The name of the Room as a String
	 * @param clientID - The integer ID of the Player that created the room (This will be the host)
	 * @param hostName - The name of the player as a String
	 * @param MACAddress - The MAC Address of the player
	 */
	public Room(String roomName, int clientID, String hostName, String MACAddress, String roomKey) {
		this.roomName = roomName;
		this.hostID = clientID;
		this.roomKey = roomKey;
		//Sets up global variables
		maxPlayerID = 0;
		players = Collections.synchronizedList( new ArrayList<Player>());
		playerIDMap = new HashMap<Integer, Integer>();
		currentGame = new Game((byte)0x00, 0, 0, 0, new double[] {0.0,0.0}, 0, 0); //Creates a new game
		System.out.println("Room " + roomKey + ": Room Created");
		lobbyLeaderboard = new Leaderboard(); //Creates a new lobbyLeaderboard
		roomState = State.LOBBY;
		addPlayer(hostName, MACAddress, clientID); //Adds the first player to the player list
	}
	
	/**
	 * Method that returns the state of the Room as a String
	 * @return - State of the Room as a String
	 */
	public String getRoomState() {
		String state = "";
		switch (roomState) {
		
			case GAME:
				state = "GAME";
				break;
				
			case LOBBY:
				state = "LOBBY";
				break;
				
			case STARTING:
				state = "STARTING";
				break;
				
			case ENDING:
				state = "ENDING";
				break;
				
			case PAUSED:
				state = "PAUSED";
				break;
				
			case FINISHED:
				state = "FINISHED";
				break;
		}
		return state;	
	}
	
	/**
	 * Method for starting the game
	 */
	public void startGame(Room gameRoom) {
		//Counts votes and set it as the current game mode
		currentGame.changeGameType(countVotes()); 
		
		switch(currentGame.getType()) {
		
		//Default Game Mode
		case Packet.GAMETYPE_DEFAULT :
			//Minimum Player Count has to be 4
			if (getPlayerCount() > 1) {
				removeTeams();
				Leaderboard gameLeaderboard = lobbyLeaderboard;
				gameLeaderboard.refresh();
				currentGame.setLeaderboard(gameLeaderboard);
				roomState = State.STARTING;
				//Broadcasts the time limit to all the Players
				TimeRemainingPacket timeRemaining = new TimeRemainingPacket();
				timeRemaining.putTimeRemaining(currentGame.getTimeLimit());
				broadcast(timeRemaining);
				//Broadcasts the boundaries to all the Player
				BoundaryUpdatePacket boundaryUpdate = new BoundaryUpdatePacket();
				double longitude = currentGame.getBoundariesCentre()[0];
				double latitude = currentGame.getBoundariesCentre()[1];
				boundaryUpdate.putBoundaryUpdate(longitude, latitude, currentGame.getBoundaryRadius());
				broadcast(boundaryUpdate);
				for (int i = 0; i < players.size(); i++) {
					players.get(i).setPreviousTarget(0);
					players.get(i).setPlayerTarget(0);
				}
				//Sends all Players their targets - MAY WANT TO WAIT A BIT UNTIL PLAYERS SPREAD OUT A BIT AS PLAYERS MIGHT FOLLOW OTHER
				for (int i = 0; i < players.size(); i++) {
					assignTargets(players.get(i).getID());
				}
				//Broadcasts the starting Leaderboard to all Players
				System.out.println("Room " + roomKey + ": Targets assigned");
				broadcastLeaderboard(false);
				//Starts the Game
				GameStartPacket gameStart = new GameStartPacket();
				broadcast(gameStart);
				//Starts the timer that will end the game when the timer is up
				if (currentGame.getTimeLimit() > 0) {
					startTimer(gameRoom, currentGame.getTimeLimit());
				}
				roomState = State.GAME;
			}
			else {
				NAKPacket nPacket = new NAKPacket();
				nPacket.notEnoughPlayers();
				Server.sendPacket(hostID, nPacket); 
			}
			break;
			
		case Packet.GAMETYPE_MAN_HUNT :
			if (getPlayerCount() > 1) {
				assignTeams(true);
				Leaderboard gameLeaderboard = lobbyLeaderboard;
				gameLeaderboard.refresh();
				currentGame.setLeaderboard(gameLeaderboard);
				roomState = State.STARTING;
				TimeRemainingPacket timeRemaining = new TimeRemainingPacket();
				timeRemaining.putTimeRemaining(currentGame.getTimeLimit());
				broadcast(timeRemaining);
				//Broadcasts the boundaries to all the Player
				BoundaryUpdatePacket boundaryUpdate = new BoundaryUpdatePacket();
				double longitude = currentGame.getBoundariesCentre()[0];
				double latitude = currentGame.getBoundariesCentre()[1];
				boundaryUpdate.putBoundaryUpdate(longitude, latitude, currentGame.getBoundaryRadius());
				broadcast(boundaryUpdate);
				for (int i = 0; i < players.size(); i++) {
					players.get(i).setPreviousTarget(0);
					players.get(i).setPlayerTarget(0);
				}
				for (int i = 0; i < players.size(); i++) {
					assignTargets(players.get(i).getID());
				}
				System.out.println("Room " + roomKey + ": Targets assigned");
				broadcastLeaderboard(false);
				//Starts the Game
				GameStartPacket gameStart = new GameStartPacket();
				broadcast(gameStart);
				//Starts the timer that will end the game when the timer is up
				if (currentGame.getTimeLimit() > 0) {
					startTimer(gameRoom, currentGame.getTimeLimit());
				}
				roomState = State.GAME;
				System.out.println("Room " + roomKey + ": Game Started");
			} else {
				NAKPacket nPacket = new NAKPacket();
				nPacket.notEnoughPlayers();
				Server.sendPacket(hostID, nPacket); 
			}
			break;
			
		case Packet.GAMETYPE_TEAM :
			
			assignTeams(false);
			break;
			
		default :
			//ERROR
			break;
				
		}
		startLocationTimer();
		//startPingTimer(Server.getRoom(roomKey), roomKey);
	}
	
	/**
	 * Method to end the game
	 */
	public synchronized void endGame() {
		//Final time broadcast gameLeaderboard so that client can determine winner
		lt.cancel();
		gameTimer.cancel();
		broadcastLeaderboard(false);
		roomState = State.ENDING;
		GameEndPacket gameEnd = new GameEndPacket();
		broadcast(gameEnd);	
		roomState = State.LOBBY;
		currentGame = new Game((byte)0x00, 0, 0, 0, new double[] {0.0,0.0}, 0, 0);
	}
	
	/**
	 * returns a player with a specific ID
	 * @param clientID - ID of the player to be returned
	 */
	public Player getPlayer(int clientID){
		return players.get(clientID);
	}
	
	/**
	 * Method for when a catch has been performed by a player
	 * @param clientID - The integer ID of the player that has performed the capture
	 */
	public void catchPerformed(int clientID) {
		int targetID = players.get(playerIDMap.get(clientID)).getTarget(); // ID of Target that has been caught
		//Packet is sent to the target to activate a button on their screen that allows them to be caught
		CaughtPacket cp = new CaughtPacket();
		players.get(clientID).setState("CHANGING");
		players.get(targetID).setPlayerPursuer(clientID);
		Server.sendPacket(targetID, cp);
		CaptureTimer cT = new CaptureTimer(roomKey, clientID, targetID);
		cT.start();
	}	
	
	///Player presses caught button but pursuer hasn't pressed button yet. - This may need changing if the caught button only appears when pursuer presses button
	/**
	 * Method for when a player has been captured
	 * @param clientID - The integer ID of the player that has been captured
	 */
	public void captured(int clientID) {
		int pursuerID = players.get(clientID).getPursuer();
		CatchSuccessPacket csp = new CatchSuccessPacket();
		csp.putSuccess((byte) 0x01);
		Server.sendPacket(pursuerID, csp);
		players.get(clientID).setPlayerPursuer(-1);
		players.get(clientID).setState("CAUGHT");
		assignTargets(pursuerID);
		assignTargets(clientID);
		players.get(clientID).setState("CONNECTED");
		players.get(pursuerID).setState("CONNECTED");
		//Notifies all players that a successful capture has occurred
		CapturePacket capturePacket = new CapturePacket();
		capturePacket.putCapture(clientID, pursuerID);
		broadcast(capturePacket);
		currentGame.getLeaderboard().updatePlayerScore(pursuerID, 100);
		lobbyLeaderboard.updatePlayerScore(pursuerID, 100);
		players.get(playerIDMap.get(pursuerID)).setPlayerScore(100);
		broadcastLeaderboard(false);
	}
	
	private void assignTargets(int clientID){
		//need to weight higher scoring players as more likely targets, also only a max of 3 pursuers.
		System.out.println("Room " + roomKey + ": Assigning target for player" + clientID);
		switch (currentGame.getType()) {
		case Packet.GAMETYPE_DEFAULT:
			ArrayList<Player> validTargets = new ArrayList<Player>();
			int previousTarget = players.get(playerIDMap.get(clientID)).getPreviousTarget();
			
			for(int i = 0; i < players.size(); i++){
				if(players.get(i).getTarget() != clientID && (i + 1) != previousTarget && i != playerIDMap.get(clientID)
				   && players.get(i).getState().equals("CONNECTED")){
					if((roomSize > 4 && players.get(i).getPursuerCount() < 3) || 
					   (roomSize == 4 && players.get(i).getPursuerCount() < 2)){
						validTargets.add(players.get(i));
					} else {
						validTargets.add(players.get(i));
						break;
					}
				}
			}
			if(validTargets.isEmpty()){
				System.out.println("Room " + roomKey + ": No valid targets for player"+clientID);
				NAKPacket np = new NAKPacket();
				np.setNAK(Packet.NAK_NO_VALID_TARGETS);
				Server.sendPacket(clientID, np);
				return;
			}
			Random rng = new Random();
			int target = rng.nextInt(validTargets.size());
			int targetID = validTargets.get(target).getID();
			System.out.println("Room " + roomKey + ": Player"+clientID+"'s target is going to be player"+targetID);
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
	
	private void assignTeams(boolean manhunt){
		int team1 = roomSize / 2;
		if(manhunt){ team1 = 1;}
		int team2 = roomSize - team1;
		Random rng = new Random();
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getState().equals("CONNECTED")){
				if(team1 == 0){
					players.get(i).setTeam(2);
					lobbyLeaderboard.updateTeam(players.get(i).getID(), 2);
					team2--;
				} else if(team2 == 0){
					players.get(i).setTeam(1);
					lobbyLeaderboard.updateTeam(players.get(i).getID(), 1);
					team1--;
				} else{
					int team = rng.nextInt(2);
					if(team == 1){
						players.get(i).setTeam(1);
						lobbyLeaderboard.updateTeam(players.get(i).getID(), 1);
						team1--;
					} else {
						players.get(i).setTeam(2);
						lobbyLeaderboard.updateTeam(players.get(i).getID(), 2);
						team2--;
					}
				}
			}
		}
		broadcastLeaderboard(true);
	}
	
	private void removeTeams(){
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getState().equals("CONNECTED")){
				lobbyLeaderboard.updateTeam(players.get(i).getID(), 0);
			}
		}
		broadcastLeaderboard(true);
	}
	
	/**
	 * Method that adds a new player to the game
	 * @param playerName - The name of the new player
	 * @param MACAddress - The MAC Address of the new player
	 * @param clientID - The integer ID of the new player
	 */
	public void addPlayer(String playerName, String MACAddress, int clientID) {
		System.out.println("Room " + roomKey + ": Adding player");
		//Checks if the room is full
		if(roomSize == 16) {
			NAKPacket np = new NAKPacket();
			np.setNAK(Packet.NAK_ROOM_FULL);
			Server.sendPacket(clientID, np);
			return;
		} else if (!getRoomState().equals("LOBBY")){
			NAKPacket np = new NAKPacket();
			np.setNAK(Packet.NAK_ROOM_IN_GAME);
			Server.sendPacket(clientID, np);
		}
		//Checks if the player's data has been previously saved
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getState().equals("DISCONNECTED") && players.get(i).getMACAddress().equals(MACAddress)) {
				players.get(i).setID(clientID);
				playerIDMap.put(clientID, i);
				players.get(i).setState("CONNECTED");
				lobbyLeaderboard.addExistingPlayer(clientID, playerName, players.get(i).getPlayerScore(), players.get(i).getTeam());
				System.out.println("Room " + roomKey + ": Client Reconnected");
				Server.sendPacket(clientID, new JoinSuccessPacket());
				NewPlayerPacket npp = new NewPlayerPacket();
				npp.putPlayerName(playerName);
				broadcast(npp);
				if(clientID > maxPlayerID) { 
					maxPlayerID = clientID; 
				}
				Server.setRoomKey(clientID, roomKey);
				roomSize++;
				return;
			}
		}
		Server.setRoomKey(clientID, roomKey);
		RoomKeyPacket rkp = new RoomKeyPacket();
		rkp.putRoomKey(roomKey);
		Server.sendPacket(clientID, rkp);
		players.add(new Player(playerName, MACAddress, clientID)); //Creates a new Player instance and adds it to list of players
		playerIDMap.put(clientID, players.size() - 1);
		lobbyLeaderboard.addPlayer(clientID, playerName);
		//Sends a New Player Packet to all Players
		JoinSuccessPacket jsp = new JoinSuccessPacket();
		jsp.putPlayerID(clientID);
		Server.sendPacket(clientID, jsp);
		NewPlayerPacket npp = new NewPlayerPacket();
		npp.putPlayerName(playerName);
		broadcast(npp);
		//Sends updated lobbyLeaderboard with new player to all Players
		broadcastLeaderboard(true);
		ScoreLimitPacket slp = new ScoreLimitPacket();
		slp.putScoreLimit(currentGame.getScoreLimit());
		TimeLimitPacket tlp = new TimeLimitPacket();
		tlp.putTimeLimit(currentGame.getTimeLimit());
		GametypePacket gtp = new GametypePacket();
		gtp.putGametype(currentGame.getType());
		Server.sendPacket(clientID, slp);
		Server.sendPacket(clientID, tlp);
		Server.sendPacket(clientID, gtp);
		//Increases the Max Player ID if its a new player
		if(clientID > maxPlayerID) { 
			maxPlayerID = clientID;
		}
		roomSize++;
		System.out.println("Room " + roomKey + ": Player"+clientID+" added");
	}
	
	/**
	 * Method that removes the player from the game
	 * @param clientID - The integer ID of the player being removed
	 */
	public void quitPlayer(int clientID, byte reason) {
		roomSize--;
		System.out.println("Room " + roomKey + ": Attempting to remove player"+ clientID);
		players.get(playerIDMap.get(clientID)).setState("DISCONNECTED");
		lobbyLeaderboard.removePlayer(clientID);
		if(!getRoomState().equals("LOBBY")){
			currentGame.removePlayer(clientID);
		}
		
		//////////////////////////////////////////
		//stop the ping timer if in game session//
		//////////////////////////////////////////
		
		//Tells all players that a player has disconnected
		DisconnectionPacket disconnectPacket = new DisconnectionPacket();
		disconnectPacket.putPlayerID(clientID);
		disconnectPacket.putDisconnectionReason(reason);
		broadcast(disconnectPacket);
		//Broadcasts updated lobbyLeaderboard
		if(getRoomState().equals("LOBBY")){
			broadcastLeaderboard(true);
		} else {
			broadcastLeaderboard(false);
		}
		//Needs to set a new host if the player is the host
		if (clientID == hostID) {
			setNewHost();			
		}
	}
	
	/**
	 * Method that changes the host's ID
	 * @param hostID - The integer ID of the new host
	 */
	public void changeHost(int hostID) {
		this.hostID = hostID;
	}
	/**
	 * Method that returns the number of active players
	 * @return Integer number of active players in the game
	 */
	public int getPlayerCount() {
		int playerCount = 0;
		for(int i = 0; i < players.size(); i++) {
			if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")) {
				playerCount++;
			}
		}
		//return roomSize;?
		return playerCount;
	}
	
	/**
	 * Method that checks which players are playing and have no been disconnected or kicked
	 * @return - List of available players
	 */
	public List<Player> getAvailablePlayerList() {
		List<Player> availablePlayers = new ArrayList<Player>();
		for(int i = 0; i < players.size(); i++) {
			if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")) {
				availablePlayers.add(players.get(i));
			}
		}
		return availablePlayers;	
	}
	
	/**
	 * Method that is used to set up a new host
	 */
	private void setNewHost() {
		ArrayList<Integer> playersAvailable = new ArrayList<Integer>(); //List of all the players that could be the new host
		for(int i = 0; i < players.size(); i++){
			//Player has to have an active state
			if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")) {
				playersAvailable.add(players.get(i).getID());
			}
		}
		Random numberGenerator = new Random();
		int players = playersAvailable.size(); //Number of players in game
		int newHost = numberGenerator.nextInt(players);
		hostID = playersAvailable.get(newHost); //Use this integer number in the arraylist to get the player in that position
		Server.sendPacket(hostID, new HostPacket()); //Alert client that they are the new host
		NewHostPacket hostPacket = new NewHostPacket();
		hostPacket.putPlayerID(hostID);
		broadcast(hostPacket); //Tells all clients that there is a new host
	}
	
	/**
	 * Method that sends a packet to all active players in the game
	 * @param broadcastPacket - The packet that needs sending
	 */
	private void broadcast(Packet broadcastPacket){
		broadcastPacket.appendlength();
		for(int i = 0; i < players.size(); i++){
			
			//Finds only active players
			if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")){
				System.out.println(broadcastPacket.toString());
				Server.broadcastPacket(players.get(i).getID(), broadcastPacket);
			}
		}
	}
	
	/**
	 * Method that creates a new lobbyLeaderboard packet instance and send the lobbyLeaderboard to all players
	 */
	private void broadcastLeaderboard(boolean lobby) {
		Packet leaderboardPacket;
		if(lobby){ 
			leaderboardPacket = new LeaderboardPacket();
			((LeaderboardPacket)leaderboardPacket).putLeaderboard(lobbyLeaderboard);
		} 
		else { 
			leaderboardPacket = new LeaderboardPacket();
			((packets.serverPackets.broadcastPackets.LeaderboardPacket) leaderboardPacket).putLeaderboard(currentGame.getLeaderboard());
		}
		//Broadcasts the updated lobbyLeaderboard to all players
		broadcast(leaderboardPacket);
	}
	
	/**
	 * Method to update the location of the player in the player instance
	 * @param location - Longtitude and Latitude of Player in an array
	 * @param clientID - The integer ID of the player
	 */
	public void setPlayerLocation(double[] location, int clientID) {
		players.get(playerIDMap.get(clientID)).setPlayerLocation(location);
	}
	
	/**
	 * Method that sets the game's time limit
	 * @param time - The time limit
	 */
	public void setTimeLimit(int time) {
		currentGame.setTimeLimit(time);
		TimeLimitPacket tlp = new TimeLimitPacket();
		tlp.putTimeLimit(time);
		broadcast(tlp);
	}
	
	/**
	 * Method that sets the game's score limit
	 * @param score - The score limit
	 */
	public void setScoreLimit(int score) {
		currentGame.setScoreLimit(score);
		ScoreLimitPacket slp = new ScoreLimitPacket();
		slp.putScoreLimit(score);
		broadcast(slp);
	}
	
	/**
	 * Method that sets the game's boundaries
	 * @param boundaries - The coordinates of the boundary centre
	 * @param radius - The radius of the boundary circle
	 */
	public void setBoundaryLimit(double[] boundariesCoordinates, int radius) {
		currentGame.setBoundariesCentre(boundariesCoordinates);
		currentGame.setBoundariesRadius(radius);
		BoundariesPacket bp = new BoundariesPacket();
		bp.putBoundaries(boundariesCoordinates[0], boundariesCoordinates[1], radius);
		broadcast(bp);
	}
	
	/**
	 * Method that sets the game's boundary interval period and the percentage the boundary decreases by
	 * @param updates - Array where first element is interval period and second element is percentage
	 */
	public void setBoundariesUpdates(int[] updates) {
		currentGame.setBoundariesUpdateInterval(updates[0]);
		currentGame.setBoundariesUpdatePercentage(updates[1]);
		packets.serverPackets.lobbyInfoPackets.BoundaryUpdatePacket bup = new packets.serverPackets.lobbyInfoPackets.BoundaryUpdatePacket();
		bup.putBoundaryUpdates(updates[0], updates[1]);
	}
	
	/**
	 * Method that allows voting to take place in a game
	 */
	public void toggleVoting() {
		currentGame.allowVoting();
		VotesPacket vp = new VotesPacket();
		votesEnabled = !votesEnabled;
		if(votesEnabled){
			vp.VotesEnabled();
		} else {
			vp.VotesDisabled();
		}
		broadcast(vp);
	}
	
	/**
	 * Method that adds a player's vote to their player instance
	 * @param vote - The game mode vote as a byte
	 * @param clientID - The integer ID of the player that has cast the vote
	 */
	public void voteGameMode(byte vote, int clientID) {
		if (currentGame.getVotingAllowed()) {
			players.get(playerIDMap.get(clientID)).setPlayerVote(vote);
		}
		//Else send packet to clients telling them that voting is not allowed ??
	}
	
	/**
	 * Method that counts all the votes and returns the winning game mode
	 * @return - The game mode that won the most votes
	 */
	private byte countVotes() {
		for(int i = 0; i < players.size(); i++){
			if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")){
				addVote(players.get(i).getPlayerVote());
			}
		}
		return currentGame.checkVoteCounts();	
	}
	
	/**
	 * Method that adds the vote byte to the count iterators in the game class
	 * @param vote - Byte that corresponds to a game type
	 */
	private void addVote(byte vote) {
		switch (vote) {
		case Packet.GAMETYPE_DEFAULT:
			currentGame.increaseSingleVoteCount();
			break;
			
		case Packet.GAMETYPE_TEAM:
			currentGame.increaseTeamVoteCount();
			break;
			
		case Packet.GAMETYPE_MAN_HUNT:
			currentGame.increaseManhuntVoteCount();
			break;
			
		default:
			//ERROR
			break;
		}
	}
	
	/**
	 * Method that changes the game mode type manually
	 * @param gameType - The game type as a byte
	 */
	public void changeGameType(byte gameType) {
		currentGame.changeGameType(gameType);
	}
	
	/**
	 * Method used to report a player. 
	 * If more than a third of players report one player then they get kicked
	 * @param reportedID - The integer ID of the reported player
	 * @param clientID - The integer ID of the player reporting the reported player
	 */
	public void report(int reportedID, int clientID) {
		//Host player cannot be removed
		if(!(reportedID == hostID)) {
			//A Player can only report 1 player at a time
			players.get(playerIDMap.get(clientID)).setReportedID(reportedID);
			//Increases the report count on the reported Player
			players.get(playerIDMap.get(reportedID)).increaseReportedCount();
			int count = 0;
			for(int i = 0; i < players.size(); i++) {
				if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED") && (players.get(i).getReportedID() == reportedID)) {
					count++;
				}
			}
			//Checks if a third of the room has reported the player
			if(count >=  (roomSize / 3)) {
				//Removes the reported player if the count is too high
				quitPlayer(reportedID, Packet.DISCONNECT_KICK);		
				//TO DO: NEEDS TO BROADCAST TO ALL PLAYERS THAT THE PLAYER HAS BEEN KICKED
				if(getRoomState().equals("LOBBY")){
					broadcastLeaderboard(true);
				} else {
					broadcastLeaderboard(false);
				}
				//Any Player that has the removed player as a reported player has their reported player reset
				for(int i = 0; i < players.size(); i++) {
					if(players.get(i).getReportedID() == reportedID) {
						players.get(playerIDMap.get(clientID)).setReportedID(-1);
					}
				}
			}
		}
	}
	
	/**
	 * Method that tells a player instance that they have acknowledged something
	 * @param clientID - The integer ID of the player acknowledging
	 */
	public void acknowledgePlayer(int clientID) {
		players.get(playerIDMap.get(clientID)).setPlayerAcknowledgement(true);
	}
	
	/**
	 * Method that allows a player to use an ability
	 * Case statement is used to distinguish between abilities and their functionality
	 * @param ability - The ability that is used
	 * @param clientID - The integer ID of the player using the ability
	 */
	public void abilityUsage(byte ability, int clientID) {
		switch (ability) {
		case Packet.ABILITY_HIDE :

			break;
			
		case Packet.ABILITY_PING :
			
			break;
			
		case Packet.ABILITY_DECOY :
			
			break;
			
		case Packet.ABILITY_SNEAK :	
			
		default:
			//ERROR
			break;
		}
	}
	
	/**
	 * Method that returns the Room Name
	 * @return - The room name as a String
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * Method that sets the Room Name
	 * @param roomName - The Room Name as a String
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	/**
	 * Method that returns the number of players active in the game
	 * @return - The number of players active in the room
	 */
	public int size(){
		return roomSize;
	}
	
	public Leaderboard getLeaderboard(){
		if(getRoomState().equals("LOBBY")){
			return lobbyLeaderboard;
		} else {
			return currentGame.getLeaderboard();
		}
	}
	
	/**
	 * Creates and starts a timer based on the time limit
	 * Room of the game that requires timer is referenced so that timer can use endGame method
	 * @param gameRoom - A reference of the room that requires the timer
	 * @param time - The time limit of the game
	 */
	public void startTimer(Room gameRoom, int time) {
		gameTimer = new Timer();
		//startPingTimer(gameRoom, roomKey);
		//Sets off the timer
		gameTimer.schedule(new EndGameTimerTask(gameRoom), (long)time*100);
	}
	
	/**
	 * 	TimerTask which calls the run function when the timer reaches its time
	 * 	Used to signalise the end of the game
	 *	@author Adam
	 */
	 static class EndGameTimerTask extends TimerTask {
		Room gameRoom;
		
		/**
		 * Constructor that's argument sets the Room global variable used in the TimerTask
		 * @param gameRoom - Room instance that is used for a global variable
		 */
		public EndGameTimerTask(Room gameRoom) {
			this.gameRoom = gameRoom;
		}
		
		/**
		 * Method is called when the TimerTask's timer has reached its time
		 */
		public void run() {
			//Ends the game
			System.out.println("Ending Game");
			gameRoom.endGame();
		}
	}
	 
	 /**
	 * Method that stops the Player timer to prevent kicking
	 * @param clientID - The integer ID of the Player
	 */
	public synchronized void interruptPlayerTimer(int clientID) {
		players.get(playerIDMap.get(clientID)).interruptPingWaitTimer();
	}
		
	/**
	 * Method that starts the ping timer where every 5 seconds it will test the ping of the players
	 * @param gameRoom - The game room instance that the timer is referring to
	 * @param roomKey - The String room key to gain access to the room
	 */
	public void startPingTimer(Room gameRoom, String roomKey) {
		Timer pingTimer = new Timer();
		//Timer repeats every 5 seconds, starting after a 1 second delay
		pingTimer.scheduleAtFixedRate(new PingTimerTask(gameRoom, roomKey), 100, 7000);
	}
	
	/**
	 * TimerTask class that sends pings out every 5 seconds to all players 
	 * and sets off waiting timers for each player in their instances
	 * @author Adam
	 *
	 */
	 static class PingTimerTask extends TimerTask {	
		String roomKey;
		Room gameRoom;
			
		/**
		 * Constructor that sets the global variables used in the TimerTask
		 * @param gameRoom - Room instance used to get the list of players
		 * @param roomKey - String room key used to gain access to the room
		 */
		public PingTimerTask(Room gameRoom, String roomKey) {
			this.gameRoom = gameRoom;
			this.roomKey = roomKey;
		}
			
		/**
		 * Method that runs every 5 seconds
		 */
		public void run() {
			//Loops to each player which is still in the game
			for(int i = 0; i < gameRoom.players.size(); i++) {
				if(!gameRoom.players.get(i).getState().equals("DISCONNECTED") && !gameRoom.players.get(i).getState().equals("KICKED")) {
					int clientID = gameRoom.players.get(i).getID();
					PingPacket pp = new PingPacket();
					Server.sendPacket(clientID, pp);
					//Starts the Ping Count Timer
					gameRoom.players.get(i).startPingWaiting(roomKey);
				}
			}
		}
	}
	 
	 public void startLocationTimer() {
			lt = new Timer();
			//Timer repeats every 5 seconds, starting after a 1 second delay
			lt.scheduleAtFixedRate(new LocationTimerTask(), 6000, 10);
		}
	 
	 class LocationTimerTask extends TimerTask {
			
		/**
		 * Method that runs every 5 seconds
		 */
		public void run() {
			//Loops to each player which is still in the game
			if(roomSize < 1){
				lt.cancel();
			}
			for(int i = 0; i < players.size(); i++) {
				if(!players.get(i).getState().equals("DISCONNECTED") && !players.get(i).getState().equals("KICKED")) {
					int clientID = players.get(i).getID();
					double[] pos = players.get(i).getPlayerLocation();
					LocationPacket lp = new LocationPacket();
					lp.putID(clientID);
					lp.putLocation(pos[0], pos[1]);
					broadcast(lp);
				}
			}
		}
	}
	 
	 public void stopLocationBroadcast(){
		 lt.cancel();
	 }
	
}