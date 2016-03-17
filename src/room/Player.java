package room;

public class Player {

	private enum playerState {
		CONNECTED, DISCONNECTED, KICKED, CAUGHT, INACTIVE, CHANGING
	}
	private String playerName;
	private int clientID;
	private double[] playerMACAddress;
	private int playerScore;
	private int playerPing;
	private int reportedID;
	private int reportedCount;
	private double[] playerLocation; //Longitude Latitude
	private byte vote; //Player's vote

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
		state = playerState.CONNECTED;
		reportedID = -1;
		reportedCount = 0;
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
		case INACTIVE:
			playerstate = "INACTIVE";
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
		pursuerCount = 0;
	}
	
	public void kickPlayer() {
		state = playerState.KICKED;
	}
	
	public void removePlayer() {
		state = playerState.DISCONNECTED;
	}
	
	public void rejoined(){
		state = playerState.CONNECTED;
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
	
	public void setID(int id){
		clientID = id;
	}
	
	public void setTeam(int team){
		playerTeam = team;
	}
	
	public double[] getMACAddress(){
		return playerMACAddress;
	}
	
	public void addPursuer(){
		pursuerCount++;
	}
	
	public int getPursuerCount(){
		return pursuerCount;
	}
	
	public int getReportedID() {
		return reportedID;
	}
	
	public void setReportedID(int reportedID) {
		this.reportedID = reportedID;
	}
	
	public void increaseReportedCount() {
		reportedCount++;
	}
	
	public int getReportedCount() {
		return reportedCount;
	}
	
	public void resetReportedCount() {
		reportedCount = 0;
	}

	public byte getPlayerVote() {
		return vote;
	}

	public void setPlayerVote(byte vote) {
		this.vote = vote;
	}
	
}
