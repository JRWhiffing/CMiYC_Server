package server.packets;

import server.packets.Packet;

//Location is a packet that can be used by both the Server and Client as in both instances it will be identical.

public class LocationPacket extends Packet {
	
	public LocationPacket(){
		super.putByte(Packet.LOCATION);
	}
	
	public LocationPacket(byte[] data){
		super.packet = data;
	}
	
	public void putLocation(double longitude, double latitude){
		super.putDouble(longitude);
		super.putDouble(latitude);
	}	
	
	public double[] getLocation(){
		return new double[]{super.getDouble(), super.getDouble()};
	}
	
}