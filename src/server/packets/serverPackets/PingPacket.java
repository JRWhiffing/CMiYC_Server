package server.packets.serverPackets;

import server.packets.Packet;

public class PingPacket extends Packet {

	public PingPacket(){
		putByte(Packet.PING);
	}
	
}