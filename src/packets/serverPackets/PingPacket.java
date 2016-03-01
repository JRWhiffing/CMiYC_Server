package packets.serverPackets;

import packets.Packet;

public class PingPacket extends Packet {

	public PingPacket(){
		putByte(Packet.PING);
	}
	
}