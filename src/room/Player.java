package room;

import java.util.Timer;
import java.util.TimerTask;

import server.Server;

/**
 * Class that contains all the Player data
 * @authors James and Adam
 *
 */
public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, CAUGHT, INACTIVE, CHANGING
	}
	private playerState state;
	
	private String playerName;
	private int clientID;
	private String playerMACAddress;
	private int playerScore;
	private int reportedID;
	private int reportedCount;
	private double[] playerLocation = new double[]{1000,1000}; //Longitude Latitude
	private byte vote; //Player's vote
	private boolean playerAcknowledgement;
	private Timer pingWaitTimer;

	private int playerTarget; //ID of Target - No Needed Anymore?
	private int previousTarget;
	private int pursuerID;//if currently being captured by a pursuer.
	private int pursuerCount;
	private int playerTeam;
	
	/**
	 * Constructor that sets up the new player
	 * @param playerName - The name of the player
	 * @param MACAddress - The MAC Address of the player
	 */
	public Player(String playerName, String MACAddress, int clientID) {
		this.playerName = playerName;
		this.playerMACAddress = MACAddress;	
		this.clientID = clientID;
		state = playerState.CONNECTED;
		playerTarget = -1;
		reportedID = -1;
		reportedCount = 0;
		playerAcknowledgement = false;
	}
	
	/**
	 * Method that returns the players state as a String
	 * @return - String that says the player's state
	 */
	public String getState() {
		String playerstate = "";
		switch (state) {
		
			case CONNECTED:
				playerstate = "CONNECTED";
				break;
				
			case DISCONNECTED:
				playerstate = "DISCONNECTED";
				break;
				
			case KICKED:
				playerstate = "KICKED";
				break;
				
			case CAUGHT:
				playerstate = "CAUGHT";
				break;
				
			case CHANGING:
				playerstate = "CHANGING";
				break;
				
			case INACTIVE:
				playerstate = "INACTIVE";
				break;
		}
		return playerstate;	
	}
	/**
	 * Method that sets the player's state
	 * @param state - The state of the player as a String
	 */
	public void setState(String state) {
		switch (state) {
		
			case "CONNECTED":
				this.state = playerState.CONNECTED;
				break;
				
			case "DISCONNECTED":
				this.state = playerState.DISCONNECTED;
				break;
				
			case "KICKED":
				this.state = playerState.KICKED;
				break;
				
			case "CAUGHT":
				this.state = playerState.CAUGHT;
				break;
				
			case "CHANGING":
				this.state = playerState.CHANGING;
				break;
				
			case "INACTIVE":
				this.state = playerState.INACTIVE;
				break;
		}
	}
	
	/**
	 * Method that interrupts the Timer task to stop Player from getting disconnected
	 */
	public void interruptPingWaitTimer() {
		pingWaitTimer.cancel();
		pingWaitTimer.purge();
	}
	
	/**
	 * Method that starts the timer that counts the ping
	 * @param roomKey - String key to game access to the room the player is in
	 */
	public void startPingWaiting(String roomKey) {
		this.pingWaitTimer = new Timer();
		pingWaitTimer.schedule(new PingWaitTimerTask(roomKey, clientID), 3000);
	}
	
	/**
	 * TimerTask class that disconnects the Player from the game if the ping is above 2 seconds
	 * @author Adam
	 *
	 */
	static class PingWaitTimerTask extends TimerTask {
		String roomKey;
		int clientID;
		
		/**
		 * Constructor that sets the global variables needed to disconnect the player from the game
		 * @param roomKey - String key to game access to the room the player is in
		 * @param clientID - Integer ID of the Player
		 */
		public PingWaitTimerTask(String roomKey, int clientID) {
			this.roomKey = roomKey;
			this.clientID = clientID;
		}
		/**
		 * Method that is run if the Server does not recieve a Ping Packet from the Player within 2 seconds
		 */
		public void run() {
			//Disconnects the Player from the Game
			System.out.println("Kicking player due to high ping");
			Server.disconnectPlayer(roomKey, clientID);
		}
	}
	
	/**
	 * Method checks whether a player has been caught
	 * @return - Boolean where returns true if the player is in state caught, else returns false
	 */
	public boolean checkCaught() {
		if(state == playerState.CAUGHT) {
			return true;
		}
		return false;
	}
	
	public boolean checkContinue() {
		if(state == playerState.CHANGING) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method that sets the Player's target
	 * @param targetID - The integer ID of the player's target
	 */
	public void setPlayerTarget(int targetID) {
		if(playerTarget != -1){
			previousTarget = playerTarget;
		}
		playerTarget = targetID;
	}
	
	public void setPlayerScore(int playerScore) {
		this.playerScore += playerScore;
	}
	
	public int getPlayerScore(){
		return playerScore;
	}

	public void setPlayerLocation(double[] playerLocation) {
		this.playerLocation = playerLocation;
	}
	
	public double[] getPlayerLocation() {
		return playerLocation;
	}
	
	public int getPreviousTarget(){
		return previousTarget;
	}
	
	public void setPreviousTarget(int clientID){
		previousTarget = clientID;
	}
	
	public int getTarget(){
		return playerTarget;
	}
	
	public int getID(){
		return clientID;
	}
	
	public void setID(int id){
		clientID = id;
	}
	
	public void setTeam(int team){
		playerTeam = team;
	}
	
	public int getTeam(){
		return playerTeam;
	}
	
	public String getMACAddress(){
		return playerMACAddress;
	}
	
	/**
	 * Method that sets the playerAcknowledgement global variable
	 */
	public void setPlayerAcknowledgement(boolean playerAcknowledgement) {
		this.playerAcknowledgement = playerAcknowledgement;
	}
	
	/**
	 * Method that returns whether the player has acknowledged or not
	 * @return Boolean of the player acknowledgement
	 */
	public boolean getPlayerAcknowledgement() {
		return playerAcknowledgement;
	}
	
	/**
	 * Method that adds another pursuer to the Player's pursuer count
	 */
	public void addPursuer() {
		pursuerCount++;
	}
	
	/**
	 * Method that resets the Player's pursuer count back to 0
	 */
	public void resetPursuerCount() {
		pursuerCount = 0;
	}
	
	/**
	 * Method that returns the Player's pursuer count
	 * @return - The Player's pursuer count
	 */
	public int getPursuerCount() {
		return pursuerCount;
	}
	
	/**
	 * setting the ID of the pursuer capturing the player
	 * @param pID - The ID of their pursuer
	 */
	public void setPlayerPursuer(int pID){
		pursuerID = pID;
	}
	
	public int getPursuer(){
		return pursuerID;
	}
	
	/**
	 * Method that sets the integer ID of the player as a global variable
	 * Each Player can only report one player at a time
	 * @param reportedID - The integer ID of the reported player
	 */
	public void setReportedID(int reportedID) {
		this.reportedID = reportedID;
	}
	
	/**
	 * Method that returns the integer ID of the Player's reported player
	 * @return - The integer ID of the Player's reported player
	 */
	public int getReportedID() {
		return reportedID;
	}
	
	/**
	 * Method that increases the number of reports the player has by 1
	 */
	public void increaseReportedCount() {
		reportedCount++;
	}
	
	/**
	 * Method that returns the number of reports the player has
	 * @return - Number of reports a player has
	 */
	public int getReportedCount() {
		return reportedCount;
	}
	
	/**
	 * Method that resets the number of reports the player has
	 */
	public void resetReportedCount() {
		reportedCount = 0;
	}

	/**
	 * Method sets the Player's vote as the global variable
	 * @param vote - The player's vote as a byte
	 */
	public void setPlayerVote(byte vote) {
		this.vote = vote;
	}
	
	/**
	 * Method that returns the player's vote
	 * @return - The player's vote as a byte
	 */
	public byte getPlayerVote() {
		return vote;
	}
	
}
