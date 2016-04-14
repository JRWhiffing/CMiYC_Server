package packets.serverPackets.broadcastPackets;

import java.util.Map.Entry;

import packets.Packet;
import room.Leaderboard;
import room.LeaderboardPlayer;

public class LeaderboardPacket extends Packet {

	public LeaderboardPacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_LEADERBOARD);
	}
	
	public LeaderboardPacket(byte[] data){
		packet = data;
	}
	
	public void putLeaderboard(Leaderboard board){ //will require a Leaderboard class.
		int size = board.getSize();
		putInt(size);//number of players in the board
		for(Entry<Integer, LeaderboardPlayer> player : board.getEntrySet()){
			putInt(player.getValue().getPlayerID());
			putString(player.getValue().getPlayerName());
			putInt(player.getValue().getPlayerScore());
			putInt(player.getValue().getPlayerTeam());
		}
	}
	
	public Leaderboard getLeaderboard(){
		int size = getInt();
		Leaderboard board = new Leaderboard();
		for(int i = 0; i < size; i++){
			board.addExistingPlayer(getInt(), getString(), getInt(), getInt());
		}
		return board;
	}
	
}