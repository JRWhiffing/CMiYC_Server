package packets.clientPackets.hostPackets;

import packets.Packet;

public class SetBoundariesPacket extends Packet {

	public SetBoundariesPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_SET_BOUNDARIES);
	}
	
	public SetBoundariesPacket(byte[] data){
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
	
	public int getBoundaryRadius(){
		return getInt();
	}
	
}