package packets.clientPackets.hostPackets;

import packets.Packet;

public class CreateRoomPacket extends Packet {

	public CreateRoomPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_CREATE_ROOM);
	}
	
	public CreateRoomPacket(byte[] data){
		packet = data;
	}
	
	public void putRoomName(String name){
		putString(name);
	}
	
	public void putMACAddress(double[] address){
		for(int i = 0; i < 6; i++){
			putDouble(address[i]);
		}
	}
	
	public String getRoomName(){
		return getString();
	}
	
	public double[] getMACAddress(){
		double[] address = new double[6];
		for(int i = 0; i < 6; i++){
			address[i] = getDouble();
		}
		return address;
	}
	
}