package room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Leaderboard {

	//player ID attached to player is not same as location in list above. Server will not know index of player,
	//only their playerID, the hashmap takes an ID and maps it to their spot in the list
	//this suggests using only a hashmap, however the packets which will take data from the leaderboard
	//will not know the player ids, only how many there are, and thus the indexes 
	private HashMap<Integer, LeaderboardPlayer> lb = new HashMap<Integer, LeaderboardPlayer>();
	
	public void addPlayer(int playerID, String playerName){
		lb.put(playerID, new LeaderboardPlayer(playerID, playerName, 0, 0));
		System.out.println("Player in leaderboard");
	}
	
	//re-adding a player who has rejoined the game or reconstructing leaderboard from packet received.
	public void addExistingPlayer(int playerID, String playerName, int score, int team){
		lb.put(playerID, new LeaderboardPlayer(playerID, playerName, score, team));
	}
	
	public void removePlayer(int playerID){
		lb.remove(playerID);
	}
	
	public void refresh(){
		for (Entry<Integer, LeaderboardPlayer> player : lb.entrySet()) {
			player.getValue().setScore(0);
		}
	}
	
	public void updatePlayerScore(int playerID, int points){
		lb.get(playerID).updateScore(points);
	}
	
	public void updateTeam(int playerID, int team){
		lb.get(playerID).updateTeam(team);
	}
	
	public int getPlayerID(int playerID){
		return lb.get(playerID).getPlayerID();
	}
	
	public String getPlayerName(int playerID){
		return lb.get(playerID).getPlayerName();
	}
	
	public int getPlayerScore(int playerID){
		return lb.get(playerID).getPlayerScore();
		
	}
	
	public int getPlayerTeam(int playerID){
		return lb.get(playerID).getPlayerTeam();
	}
	
	public int getSize(){
		return lb.size();
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
		for (Entry<Integer, LeaderboardPlayer> player : lb.entrySet()) {
			if (player.getValue().getPlayerScore() >= scoreLimit) {
				return true;
			}
		}
		return false;
	}
	
	public Set<Entry<Integer, LeaderboardPlayer>> getEntrySet(){
		return lb.entrySet();
	}
	
	//bubble sort.
//	private void sortLeaderboard(){
//		LeaderboardPlayer tempPlayer;
//		if(leaderboard.size() < 2){
//			return;
//		}
//		for(int i = 0; i < leaderboard.size() - 1; i++){
//			for(int j = leaderboard.size() - 1; j >= i + 1; j--){
//				if(leaderboard.get(j).getPlayerScore() < leaderboard.get(j - 1).getPlayerScore()){
//					tempPlayer = leaderboard.get(j);
//					leaderboard.add(j, leaderboard.get(j - 1));
//					leaderboard.add(j - 1, tempPlayer);
//				}
//			}
//		}
//	}
	
	@Override
	public String toString(){
		String leaderBoard = "";
		
		for (Entry<Integer, LeaderboardPlayer> player : lb.entrySet()) {
			leaderBoard += "Player: " + player.getValue().getPlayerID() + "\n";
			leaderBoard += "\tName: " +  player.getValue().getPlayerName() + "\n";
			leaderBoard += "\tScore: " +  player.getValue().getPlayerScore() + "\n";
			leaderBoard += "\tTeam: " +  player.getValue().getPlayerTeam() + "\n";
		}
		
		return leaderBoard;
	}
	
}