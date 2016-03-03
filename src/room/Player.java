package room;

public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, SETTINGUP
	}
	private String playerName;
	private double[] playerMACAddress;
	private int playerScore;
	private double playertarget; //ID of Target
	private double playerLocationLatitude;
	private double playerLocationLongtitude;
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
	
	
}
