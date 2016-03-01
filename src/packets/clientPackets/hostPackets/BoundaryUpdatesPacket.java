package packets.clientPackets.hostPackets;

import packets.Packet;

public class BoundaryUpdatesPacket extends Packet {

	public BoundaryUpdatesPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_BOUNDARY_UPDATES);
	}
	
	public BoundaryUpdatesPacket(byte[] data){
		packet = data;
	}
	
	public void putBoundaryUpdates(int interval, int percentage){
		putInt(interval);
		putInt(percentage);
	}
	
	public int[] getBoundaryUpdates(){
		return new int[]{getInt(), getInt()};
	}
	
}