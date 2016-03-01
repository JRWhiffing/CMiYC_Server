package packets.serverPackets;

import packets.Packet;

public class HostPacket extends Packet {

	public HostPacket(){
		putByte(Packet.HOST);
	}
	
}