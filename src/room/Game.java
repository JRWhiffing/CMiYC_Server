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
	private double[] boundariesCentre;
	private int boundaryRadius;
	private int boundariesUpdateInterval;
	private int boundariesUpdatePercentage;
	private int playerCountLimit;
	private boolean votingAllowed;
	
	private int teamVoteCount = 0;
	private int singleVoteCount = 0;
	private int manhuntVoteCount = 0;
	
	
	public Game(byte type, int score, int time, int boundaryRadius, double[] boundariesCentre, int bUpdateInterval, int bUpdatePercentage){
		changeGameType(type);
		scoreLimit = score;
		timeLimit = time;
		this.boundariesCentre = boundariesCentre;
		boundariesUpdateInterval = bUpdateInterval;
		boundariesUpdatePercentage = bUpdatePercentage;
		votingAllowed = false;
	}
	
	
	public void increaseTeamVoteCount() {
		teamVoteCount++;
	}
	
	public void increaseSingleVoteCount() {
		singleVoteCount++;
	}
	
	public void increaseManhuntVoteCount() {
		manhuntVoteCount++;
	}
	
	/**
	 * Method that compares all the iterators and declares a winning game mode
	 * If there is a draw then the default game mode is returned
	 * @return - The winning game mode as a byte
	 */
	public byte checkVoteCounts() {
		if((teamVoteCount > singleVoteCount) && (teamVoteCount > manhuntVoteCount)) {
			return Packet.GAMETYPE_TEAM;
		}
		if((manhuntVoteCount > singleVoteCount) && (manhuntVoteCount > teamVoteCount)) {
			return Packet.GAMETYPE_MAN_HUNT;
		}
		return Packet.GAMETYPE_DEFAULT;
	}
	
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
	
	public void setBoundariesRadius(int boundaryRadius) {
		this.setBoundaryRadius(boundaryRadius);
	}
	
	public double[] getBoundariesCentre() {
		return boundariesCentre;
	}
	
	public void setBoundariesCentre(double[] boundariesCentre) {
		this.boundariesCentre = boundariesCentre;
	}
	
	public double getBoundariesUpdateInterval() {
		return boundariesUpdateInterval;
	}
	public void setBoundariesUpdateInterval(int boundariesUpdateInterval) {
		this.boundariesUpdateInterval = boundariesUpdateInterval;
	}
	public double getBoundariesUpdatePercentage() {
		return boundariesUpdatePercentage;
	}
	public void setBoundariesUpdatePercentage(int boundariesUpdatePercentage) {
		this.boundariesUpdatePercentage = boundariesUpdatePercentage;
	}
	public int getPlayerCountLimit() {
		return playerCountLimit;
	}
	public void setPlayerCountLimit(int playerCountLimit) {
		this.playerCountLimit = playerCountLimit;
	}
	public void allowVoting() {
		this.votingAllowed = true;
	}
	public boolean getVotingAllowed() {
		return votingAllowed;
	}
	
	
	
	
	//Gametype
	//Score Limit
	//Time Limit
	//Team?
	//Boundaries
	//Boundary Update Interval
	//Boundary Update Percentage
	//etc.
	
	public void changeGameType(byte type) {
		switch (type) {
		
		case Packet.GAMETYPE_TEAM:
			gameType = GameType.TEAM;
			break;
			
		case Packet.GAMETYPE_DEFAULT:
			gameType = GameType.SINGLE;
			break;
			
		case Packet.GAMETYPE_MAN_HUNT:
			gameType = GameType.MANHUNT;
			break;
			
		default:
			gameType = GameType.SINGLE; //Might not be needed
			break;
			
		}
	}

	public byte getType() {
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


	public int getBoundaryRadius() {
		return boundaryRadius;
	}


	public void setBoundaryRadius(int boundaryRadius) {
		this.boundaryRadius = boundaryRadius;
	}
	
}