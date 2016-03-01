package packets.serverPackets.broadcastPackets;

import packets.Packet;

public class NewPlayerPacket extends Packet {

	public NewPlayerPacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_NEW_PLAYER);
	}
	
	public NewPlayerPacket(byte[] data){
		packet = data;
	}

	public void putPlayerName(String name){
		putString(name);
	}
	
	public String getPlayerName(){
		return getString();
	}
	
}