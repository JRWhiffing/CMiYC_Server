package packets.clientPackets.hostPackets;

import packets.Packet;

public class ScoreLimitPacket extends Packet {

	public ScoreLimitPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_SCORE_LIMIT);
	}
	
	public ScoreLimitPacket(byte[] data){
		packet = data;
	}
	
	public void putScoreLimit(int score){//score in 10s/100s/1,000s?
		putInt(score);
	}
}