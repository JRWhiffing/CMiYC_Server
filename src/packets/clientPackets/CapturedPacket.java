package packets.clientPackets;

import packets.Packet;

public class CapturedPacket extends Packet {

	public CapturedPacket(){
		putByte(Packet.CAPTURED);
	}
	
}