package packets.serverPackets.lobbyInfoPackets;

import packets.Packet;

public class BoundaryUpdatePacket extends Packet {
	public BoundaryUpdatePacket(){
		putByte(Packet.LOBBYINFO);
		putByte(Packet.LOBBYINFO_BOUNDARY_UPDATES);
	}
	
	public BoundaryUpdatePacket(byte[] data){
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
