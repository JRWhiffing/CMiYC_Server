package room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Leaderboard {

	private List<LeaderboardPlayer> leaderboard = Collections.synchronizedList( new ArrayList<LeaderboardPlayer>());
	//player ID attached to player is not same as location in list above. Server will not know index of player,
	//only their playerID, the hashmap takes an ID and maps it to their spot in the list
	//this suggests using only a hashmap, however the packets which will take data from the leaderboard
	//will not know the player ids, only how many there are, and thus the indexes 
	private HashMap<Integer, Integer> playerIDMap = new HashMap<Integer, Integer>();
	private int maxPlayerID = 0;
	
	public void addPlayer(int playerID, String playerName){
		leaderboard.add(new LeaderboardPlayer(playerID, playerName, 0, 0));
		playerIDMap.put(playerID, leaderboard.size() - 1);
		System.out.println("Adding a player to the leaderboard");
		if(playerID > maxPlayerID){ maxPlayerID = playerID; }
	}
	
	//re-adding a player who has rejoined the game or reconstructing leaderboard from packet received.
	public void addExistingPlayer(int playerID, String playerName, int score, int team, boolean sort){
		leaderboard.add(new LeaderboardPlayer(playerID, playerName, score, team));
		playerIDMap.put(playerID, leaderboard.size() - 1);
		if(playerID > maxPlayerID){ maxPlayerID = playerID; }
		if(sort){sortLeaderboard();}
	}
	
	public void removePlayer(int playerID){
		leaderboard.remove(playerIDMap.get(playerID));
		int tempMaxPlayerID = 0;
		for(int i = playerIDMap.get(playerID); i <= maxPlayerID; i++){
			if(playerIDMap.containsKey(i)){
				if(i > tempMaxPlayerID) {tempMaxPlayerID = i;}
				playerIDMap.put(i, playerIDMap.get(i)-1);
			}
		}
		playerIDMap.remove(playerID);
		maxPlayerID = tempMaxPlayerID;
	}
	
	public void updatePlayerScore(int playerID, int points){
		leaderboard.get(playerIDMap.get(playerID)).updateScore(points);
		sortLeaderboard();
	}
	
	public void updateTeam(int playerID, int team){
		leaderboard.get(playerIDMap.get(playerID)).updateScore(team);
	}
	
	public int getPlayerID(int playerID){
		return leaderboard.get(playerID).getPlayerID();
	}
	
	public String getPlayerName(int playerID){
		return leaderboard.get(playerID).getPlayerName();
	}
	
	public int getPlayerScore(int playerID){
		return leaderboard.get(playerID).getPlayerScore();
		
	}
	
	public int getPlayerTeam(int playerID){
		return leaderboard.get(playerID).getPlayerTeam();
	}
	
	public int getSize(){
		return leaderboard.size();
	}
	
	/**
	 * Method that checks whether a Player has reached the score limit
	 * Loops the whole leaderboard and checks every player
	 * @param scoreLimit - Integer number scorelimit
	 * @return - Boolean that tells whether the limit has been reached or not
	 */
	public boolean checkScoreLimitReached(int scoreLimit) {
		//If no score limit has been set
		if (scoreLimit < 1) {
			return false;
		}
		//Loops every player in the leaderboard
		for (int i = 0; i < leaderboard.size(); i++) {
			if (leaderboard.get(i).getPlayerScore() >= scoreLimit) {
				return true;
			}
		}
		return false;
	}
	
	//bubble sort.
	private void sortLeaderboard(){
		LeaderboardPlayer tempPlayer;
		for(int i = 0; i < leaderboard.size(); i++){
			for(int j = leaderboard.size(); j > i + 1; j--){
				if(leaderboard.get(j).getPlayerScore() < leaderboard.get(j - 1).getPlayerScore()){
					tempPlayer = leaderboard.get(j);
					leaderboard.add(j, leaderboard.get(j - 1));
					leaderboard.add(j - 1, tempPlayer);
				}
			}
		}
	}
	
	@Override
	public String toString(){
		String leaderBoard = "";
		
		for (int i = 0; i < leaderboard.size(); i++){
			leaderBoard += "Player: " + leaderboard.get(i).getPlayerID() + "\n";
			leaderBoard += "\tName: " + leaderboard.get(i).getPlayerName() + "\n";
			leaderBoard += "\tScore: " + leaderboard.get(i).getPlayerScore() + "\n";
			leaderBoard += "\tTeam: " + leaderboard.get(i).getPlayerTeam() + "\n";
		}
		
		return leaderBoard;
	}
	
}