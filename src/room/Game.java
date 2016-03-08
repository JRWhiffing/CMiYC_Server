package room;

import packets.Packet;

public class Game {

	private enum GameType {
		TEAM, SINGLE, MANHUNT //etc.
	}
	private GameType gameType;
	private Leaderboard leaderboard;
	private int scoreLimit;
	private int timeLimit;
	private double[] boundaries;
	private double boundariesUpdateInterval;
	private double boundariesUpdatePercentage;
	private int playerCountLimit;
	
	
	public int getScoreLimit() {
		return scoreLimit;
	}
	public void setScoreLimit(int scoreLimit) {
		this.scoreLimit = scoreLimit;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public double getBoundaries() {
		return boundaries;
	}
	public void setBoundaries(double boundaries) {
		this.boundaries = boundaries;
	}
	public double getBoundariesUpdateInterval() {
		return boundariesUpdateInterval;
	}
	public void setBoundariesUpdateInterval(double boundariesUpdateInterval) {
		this.boundariesUpdateInterval = boundariesUpdateInterval;
	}
	public double getBoundariesUpdatePercentage() {
		return boundariesUpdatePercentage;
	}
	public void setBoundariesUpdatePercentage(double boundariesUpdatePercentage) {
		this.boundariesUpdatePercentage = boundariesUpdatePercentage;
	}
	public int getPlayerCountLimit() {
		return playerCountLimit;
	}
	public void setPlayerCountLimit(int playerCountLimit) {
		this.playerCountLimit = playerCountLimit;
	}
	
	
	
	
	//Gametype
	//Score Limit
	//Time Limit
	//Team?
	//Boundaries
	//Boundary Update Interval
	//Boundary Update Percentage
	//etc.
	
	public Game(byte type, int score, int time, double[] boundaries, double bUpdateInterval, double bUpdatePercentage){
		changeGameType(type);
		scoreLimit = score;
		timeLimit = time;
		this.boundaries = boundaries;
		boundariesUpdateInterval = bUpdateInterval;
		boundariesUpdatePercentage = bUpdatePercentage;
	}
	
	public void changeGameType(byte type){
		switch (type){
		case Packet.GAMETYPE_TEAM:
			gameType = GameType.TEAM;
			break;
		case Packet.GAMETYPE_DEFAULT:
			gameType = GameType.SINGLE;
			break;
		case Packet.GAMETYPE_MAN_HUNT:
			gameType = GameType.MANHUNT;
			break;
		}
	}

	public byte getType(){
		byte type = 0x00;
		switch (gameType) {
		case TEAM:
			type = Packet.GAMETYPE_TEAM;
			break;
		case SINGLE:
			type = Packet.GAMETYPE_DEFAULT;
			break;
		case MANHUNT:
			type = Packet.GAMETYPE_MAN_HUNT;
			break;
		}
		return type;
	}
	
}