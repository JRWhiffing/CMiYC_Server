package packets.clientPackets;

import packets.Packet;

public class BadSpawnPacket extends Packet {

	public BadSpawnPacket(){
		putByte(Packet.BAD_SPAWN);
	}
	
}