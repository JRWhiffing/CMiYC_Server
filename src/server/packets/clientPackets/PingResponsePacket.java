package server.packets.clientPackets;

import server.packets.Packet;

public class PingResponsePacket extends Packet {
	
	public PingResponsePacket(){
		super.putByte(Packet.PING_RESPONSE);
	}
	
}