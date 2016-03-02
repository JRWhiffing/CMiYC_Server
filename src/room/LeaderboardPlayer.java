package room;

public class LeaderboardPlayer {

	private final int id;
	private final String name;
	private int score;
	private int team;
	private int ping;
	
	public LeaderboardPlayer(int playerID, String playerName, int score, int team, int ping){	
		id = playerID;
		name = playerName;
		this.score = score;
		this.team = team;
		this.ping = ping;
	}
	
	public void updateScore(int points){
		score += points;
	}
	
	public void updateTeam(int team){
		this.team = team;
	}
	
	public void updatPing(int ping){
		this.ping = ping;
	}
	
	public int getPlayerID(){
		return id;
	}
	
	public String getPlayerName(){
		return name;
	}
	
	public int getPlayerScore(){
		return score;
	}
	
	public int getPlayerTeam(){
		return team;
	}
	
	public int getPlayerPing(){
		return ping;
	}
	
}