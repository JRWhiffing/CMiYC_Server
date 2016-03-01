package server.packets.clientPackets;

import server.packets.Packet;

public class PingResponsePacket extends Packet {
	
	public PingResponsePacket(){
		putByte(Packet.PING_RESPONSE);
	}
	
}