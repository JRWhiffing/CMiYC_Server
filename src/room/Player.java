package room;

public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, SETTINGUP
	}
	private String playerName;
	private int clientID;
	private double[] playerMACAddress;
	private int playerScore;
	private int playerPing;
	private int playerTarget = -1; //ID of Target - No Needed Anymore?
	private int previousTarget;
	private double[] playerLocation; //Latitude Longitude
	private int pursuerCount;
	private int playerTeam;
	
	
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


	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}

	public double[] getPlayerLocation() {
		return playerLocation;
	}

	public void setPlayerLocation(double[] playerLocation) {
		this.playerLocation = playerLocation;
	}
	
	public void removePlayer() {
		//playerState = DISCONNECTED;
	
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
