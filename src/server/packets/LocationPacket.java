package server.packets;

import server.packets.Packet;

//Location is a packet that can be used by both the Server and Client as in both instances it will be identical.

public class LocationPacket extends Packet {
	
	public LocationPacket(){
		putByte(Packet.LOCATION);
	}
	
	public LocationPacket(byte[] data){
		packet = data;
	}
	
	public void putLocation(double longitude, double latitude){
		putDouble(longitude);
		putDouble(latitude);
	}	
	
	public double[] getLocation(){
		return new double[]{getDouble(), getDouble()};
	}
	
}