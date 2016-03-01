package server.packets.clientPackets;

import server.packets.Packet;

public class QuitPacket extends Packet {
	
	public QuitPacket(){
		putByte(Packet.QUIT);
	}
	
}