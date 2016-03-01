package server.packets.clientPackets;

import server.packets.Packet;

public class VotePacket extends Packet {
	
	public VotePacket(){
		putByte(Packet.VOTE);
	}
	
	public VotePacket(byte[] data){
		packet = data;
	}
	
	public void putVote(byte vote){
		putByte(vote);
	}
	
//	public void defaultGametype(){
//		putByte(Packet.GAMETYPE_DEFAULT);
//	}
//	
//	public void manHuntGametype(){
//		putByte(Packet.GAMETYPE_MAN_HUNT);
//	}
//	
//	public void teamGametype(){
//		putByte(Packet.GAMETYPE_TEAM);
//	}
	
	public byte getVote(){
		return getByte();
	}
	
}