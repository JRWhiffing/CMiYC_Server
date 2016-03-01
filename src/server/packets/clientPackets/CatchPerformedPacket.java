package server.packets.clientPackets;

import server.packets.Packet;

public class CatchPerformedPacket extends Packet {

	public CatchPerformedPacket(){
		putByte(Packet.CATCH_PERFORMED);
	}
	
}