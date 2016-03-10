package room;

public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, CAUGHT, CHANGING
	}
	private String playerName;
	private int clientID;
	private double[] playerMACAddress;
	private int playerScore;
	private int playerPing;
	private double[] playerLocation; //Longitude Latitude

	private int playerTarget = -1; //ID of Target - No Needed Anymore?
	private int previousTarget;
	private int pursuerCount;
	private int playerTeam;
	private playerState state;
	
	
	//Connected (i.e. they haven't lost connection)
	//Name
	//ID/MAC Address
	//Score
	//Target
	//Location?
	//etc.
	
	public Player(String playerName, double[] MACAddress) {
		this.playerName = playerName;
		playerMACAddress = MACAddress;	
	}
	
	public String getState(){
		String playerstate = "";
		switch (state){
		case CONNECTED:
			playerstate = "CONNNECTED";
			break;
		case DISCONNECTED:
			playerstate = "DISCONNNECTED";
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
		}
		return playerstate;	
	}
	
	public boolean checkCaught() {
		if(state == playerState.CAUGHT) {
			return true;
		}
		return false;
	}
	
	public void beenCaught() {
		state = playerState.CHANGING;
	}
	
	public void captured() {
		state = playerState.CAUGHT;
	}
	
	public void removePlayer() {
		state = playerState.DISCONNECTED;
	}

	public void setPlayerTarget(int targetID){
		if(playerTarget != -1){
			previousTarget = playerTarget;
		}
		playerTarget = targetID;
	}

	public int getPlayerScore() {
		return playerScore;
	}
	
	public void setPlayerPing(int playerPing) {
		this.playerPing = playerPing;
	}
	
	public boolean checkContinue() {
		if(state == playerState.CHANGING) {
			return true;
		}
		return false;
	}
	
	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}

	public double[] getPlayerLocation() {
		return playerLocation;
	}

	public void setPlayerLocation(double[] playerLocation) {
		this.playerLocation = playerLocation;
	}
	
	public int getPreviousTarget(){
		return previousTarget;
	}
	
	public int getTarget(){
		return playerTarget;
	}
	
	public int getID(){
		return clientID;
	}
}
