package room;

public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, SETTINGUP, CAUGHT, CHANGING
	}
	private String playerName;
	private double[] playerMACAddress;
	private int playerScore;
	private double playerPing;
	private double[] playerLocation; //Longitude Latitude
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
	
	public void setPlayerPing(double playerPing) {
		this.playerPing = playerPing;
	}
	
	public boolean checkContinue() {
		if(state == playerState.CHANGING) {
			return true;
		}
		return false;
	}

	public int getPlayerScore() {
		return playerScore;
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
	
}
