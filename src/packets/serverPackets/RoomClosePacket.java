package packets.serverPackets;

import packets.Packet;

public class RoomClosePacket extends Packet {

	public RoomClosePacket(){
		putByte(Packet.ROOM_CLOSE);
	}
	
}