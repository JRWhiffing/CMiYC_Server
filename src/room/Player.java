package room;

public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, SETTINGUP
	}
	private String playerName;
	private double[] playerMACAddress;
	private int playerScore;
	private double playerPing;
	private double playertarget; //ID of Target - No Needed Anymore?
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

	public int getPlayerScore() {
		return playerScore;
	}
	
	public void setPlayerPing(double playerPing) {
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
	
	
	
}
