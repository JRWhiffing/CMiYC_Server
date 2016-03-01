package server.packets.clientPackets.hostPackets;

import server.packets.Packet;

public class HostStartRoundPacket extends Packet {

	public HostStartRoundPacket(){
		putByte(Packet.HOST_ACTION_START_ROUND);
	}
	
}