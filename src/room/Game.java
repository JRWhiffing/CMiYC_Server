package room;

public class Game {

	private enum GameType {
		TEAM, SINGLE //etc.
	}
	private int scoreLimit;
	private int timeLimit;
	private double boundaries;
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
	
}
