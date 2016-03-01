package packets.serverPackets.lobbyInfoPackets;

import packets.Packet;

public class BoundariesPacket extends Packet {

	public BoundariesPacket(){
		putByte(Packet.LOBBYINFO);
		putByte(Packet.LOBBYINFO_BOUNDARIES);
	}
	
	public BoundariesPacket(byte[] data){
		packet = data;
	}
	
	public void putBoundaries(double longitude, double latitude, int radius){
		putDouble(longitude);
		putDouble(latitude);
		putInt(radius);
	}
	
	public double[] getBoundariesCentre(){
		return new double[]{getDouble(), getDouble()};
	}
	
	public int getBoundariesRadius(){
		return getInt();
	}
	
}