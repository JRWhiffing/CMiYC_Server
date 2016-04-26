package packets.clientPackets.hostPackets;

import packets.Packet;

public class CreateRoomPacket extends Packet {

	public CreateRoomPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_CREATE_ROOM);
	}
	
	public CreateRoomPacket(byte[] data){
		System.out.println("Setting data");
		packet = data;
	}
	
	public void putRoomName(String name){
		putString(name);
	}
	
	public void putHostName(String name){//the nickname of the player creating the room
		putString(name);
	}
	
	public void putMACAddress(String address){
		putString(address);
	}
	
	public String getRoomName(){
		return getString();
	}
	
	public String getHostName(){
		return getString();
	}
	
	public String getMACAddress(){
		return getString();
	}
	
}