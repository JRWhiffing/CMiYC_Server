package packets.clientPackets;

import packets.Packet;

public class QuitPacket extends Packet {
	
	public QuitPacket(){
		putByte(Packet.QUIT);
	}
	
}