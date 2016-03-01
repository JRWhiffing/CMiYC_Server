package packets.serverPackets;

import packets.Packet;

public class GameEndPacket extends Packet {

	public GameEndPacket(){
		putByte(Packet.GAME_END);
	}
	
}