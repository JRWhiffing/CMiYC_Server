package packets.clientPackets;

import packets.Packet;

public class CatchPerformedPacket extends Packet {

	public CatchPerformedPacket(){
		putByte(Packet.CATCH_PERFORMED);
	}
	
}