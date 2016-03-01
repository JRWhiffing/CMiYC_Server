package server.packets;

import server.packets.Packet;

public class LocationPacket extends Packet {
	
	public LocationPacket(){
		super.putByte((byte)0x01);
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