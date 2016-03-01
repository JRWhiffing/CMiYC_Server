package packets.clientPackets.hostPackets;

import packets.Packet;

public class AllowVotingPacket extends Packet {

	public AllowVotingPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_ALLOW_VOTING);
	}
	
}