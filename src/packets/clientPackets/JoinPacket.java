package packets.clientPackets;

import packets.Packet;

public class JoinPacket extends Packet {

	public JoinPacket(){
		putByte(Packet.JOIN);
	}
	
	public JoinPacket(byte[] data){
		packet = data;
	}
	
	public void putRoomKey(String key){
		putString(key);
	}
	
	public void putPlayerName(String name){//the name of the player joining the room
		putString(name);
	}
	
	public void putMACAddress(String address){
		putString(address);
	}
	
	public String getRoomKey(){
		return getString();
	}
	
	public String getPlayerName(){
		return getString();
	}
	
	public String getMACAddress(){
		return getString();
	}
	
}