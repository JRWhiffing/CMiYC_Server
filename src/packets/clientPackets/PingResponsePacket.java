package packets.clientPackets;

import packets.Packet;

public class PingResponsePacket extends Packet {
	
	public PingResponsePacket(){
		putByte(Packet.PING_RESPONSE);
	}
	
}