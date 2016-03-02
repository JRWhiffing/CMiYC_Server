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
		leaderboard.add(new LeaderboardPlayer(playerID, playerName, 0, 0, 0));
		playerIDMap.put(playerID, leaderboard.size() - 1);
		if(playerID > maxPlayerID){ maxPlayerID = playerID; }
	}
	
	//re-adding a player who has rejoined the game or reconstructing leaderboard from packet received.
	public void addExistingPlayer(int playerID, String playerName, int score, int team, int ping){
		leaderboard.add(new LeaderboardPlayer(playerID, playerName, score, team, ping));
		playerIDMap.put(playerID, leaderboard.size() - 1);
		if(playerID > maxPlayerID){ maxPlayerID = playerID; }
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
		maxPlayerID = tempMaxPlayerID;
	}
	
	public void updatePlayerScore(int playerID, int points){
		leaderboard.get(playerIDMap.get(playerID)).updateScore(points);
	}
	
	public void updateTeam(int playerID, int team){
		leaderboard.get(playerIDMap.get(playerID)).updateScore(team);
	}
	
	public void updatPing(int playerID, int ping){
		leaderboard.get(playerIDMap.get(playerID)).updateScore(ping);
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
	
	public int getPlayerPing(int playerID){
		return leaderboard.get(playerID).getPlayerPing();
	}
	
	public int getSize(){
		return leaderboard.size();
	}
	
}