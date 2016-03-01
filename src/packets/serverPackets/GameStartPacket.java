package packets.serverPackets;

import packets.Packet;

public class GameStartPacket extends Packet {

	public GameStartPacket(){
		putByte(Packet.GAME_START);
	}
	
}