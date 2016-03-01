package packets.serverPackets;

import packets.Packet;

public class KickPacket extends Packet {
	
	public KickPacket(){
		putByte(Packet.KICK);
	}
	
	public KickPacket(byte[] data){
		packet = data;
	}
	
	public void putKickReason(byte reason){
		putByte(reason);
	}
	
	//Needed?
//	public void putReasonKicked(){
//		putByte(Packet.KICK_KICKED);
//	}
//	
//	public void putReasonPoorConnection(){
//		putByte(Packet.KICK_POOR_CONNECTION);
//	}
	
	public byte getKickReason(){
		return getByte();
	}
	
}