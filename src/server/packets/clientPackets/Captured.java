package server.packets.clientPackets;

import server.packets.Packet;

public class Captured extends Packet {

	public Captured(){
		putByte(CAPTURED);
	}
	
}