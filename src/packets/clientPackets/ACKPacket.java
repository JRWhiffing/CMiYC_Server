package packets.clientPackets;

import packets.Packet;

public class ACKPacket extends Packet {

	public ACKPacket(){
		putByte(Packet.ACK);
	}
	
}