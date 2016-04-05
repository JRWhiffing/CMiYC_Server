package packets.serverPackets;

import packets.Packet;

public class CaughtPacket extends Packet {

	public CaughtPacket() {
		putByte(Packet.CAUGHT);
	}
}
