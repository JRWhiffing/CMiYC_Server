package packets.serverPackets.broadcastPackets;

import packets.Packet;

public class LeaderboardPacket extends Packet {

	public LeaderboardPacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_LEADERBOARD);
	}
	
	public LeaderboardPacket(byte[] data){
		packet = data;
	}
	
//	public void putLeaderboard(Leaderboard board){ //will require a Leaderboard class.
//		int size = board.getSize()
//		putInt(size);//number of players in the board
//		for(int i = 0; i < size; i++){
//			putInt(board.getPlayer(i).getPlayerID());
//			putString(board.getPlayer(i).getPlayerName());
//			putInt(board.getPlayer(i).getPlayerScore());
//			putInt(board.getPlayer(i).getPlayerPing());
//		}
//	}
	
//	public Leaderboard getLeaderboard(){
//		int size = getInt();
//		Leaderboard board = new Leaderboard();
//		for(int i = 0; i < size; i++){
//			Leaderboard.addPlayer(getInt(), getString(), getInt(), getInt());
//		}
//	}
	
}