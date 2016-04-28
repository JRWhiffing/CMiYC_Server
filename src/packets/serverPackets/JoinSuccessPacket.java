package packets.serverPackets;

import packets.Packet;

public class JoinSuccessPacket extends Packet {
	
	public JoinSuccessPacket(){
		putByte(Packet.JOIN_SUCCESS);
	}
	
	public JoinSuccessPacket(byte[] data){
		packet = data;
	}
	
	public void putPlayerID(int id){
		putInt(id);
	}
	
	public int getPlayerID(){
		return getInt();
	}
	
}
