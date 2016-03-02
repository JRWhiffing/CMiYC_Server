package room;

public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, INVITED
	}
	private String playerName;
	private double playerId;
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
	
}
