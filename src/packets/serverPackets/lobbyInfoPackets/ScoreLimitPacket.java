package packets.serverPackets.lobbyInfoPackets;

import packets.Packet;

public class ScoreLimitPacket extends Packet {

	public ScoreLimitPacket(){
		putByte(Packet.LOBBYINFO);
		putByte(Packet.LOBBYINFO_SCORE_LIMIT);
	}
	
	public ScoreLimitPacket(byte[] data){
		packet = data;
	}
	
	public void putScoreLimit(int score){
		putInt(score);
	}
	
	public int getScoreLimit(){
		return getInt();
	}
	
}