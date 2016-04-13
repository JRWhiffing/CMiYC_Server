package packets.serverPackets;

import packets.Packet;

public class JoinSuccessPacket extends Packet {
	
	public JoinSuccessPacket(){
		putByte(Packet.JOIN_SUCCESS);
	}
	
}
