package packets.clientPackets;

import packets.Packet;

public class PlayerReadyPacket extends Packet {

	public PlayerReadyPacket(){
		putByte(Packet.PLAYER_READY);
	}
	
}