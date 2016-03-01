package packets.clientPackets;

import packets.Packet;

public class Captured extends Packet {

	public Captured(){
		putByte(CAPTURED);
	}
	
}