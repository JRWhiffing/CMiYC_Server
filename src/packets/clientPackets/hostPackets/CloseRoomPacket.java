package packets.clientPackets.hostPackets;

import packets.Packet;

public class CloseRoomPacket extends Packet {

	public CloseRoomPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_CLOSE_ROOM);
	}
	
}