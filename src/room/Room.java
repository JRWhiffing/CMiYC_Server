package room;

public class Room {

	private int roomNumber;
	private String roomName;
	private Game currentGame;
	private enum State {
		GAME, LOBBY, STARTING, ENDING, PAUSED, INTERRUPTED
	}
	private int voteCount;
	private double hostID; //Mac Address of Host
	private Leaderboard leaderboard;
	
	//Players
	//Game (Object?)
	//Name
	//Key
	//State? i.e. Game , Lobby , Starting , Ending, etc.
	//Host?
	//Voting
	//Votes
	//etc.
	
	public int getRoomNumber(){
		return roomNumber;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}
	
	public double getHostID() {
		return hostID;
	}

	public void setHostID(double hostID) {
		this.hostID = hostID;
	}
	
}