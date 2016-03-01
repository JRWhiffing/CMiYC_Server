package server.packets.clientPackets;

import server.packets.Packet;

public class Captured extends Packet {

	public Captured(){
		super.putByte(CAPTURED);
	}
	
}