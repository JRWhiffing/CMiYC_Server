package server.packets.serverPackets;

import server.packets.Packet;

public class NAKPacket extends Packet {
	
	public NAKPacket(){
		putByte(Packet.SERVER_NAK);
	}
	
	public NAKPacket(byte[] data){
		packet = data;
	}
	
	public void invalidRoomKey(){
		putByte(Packet.NAK_INVALID_ROOM_KEY);
	}
	
	public void notEnoughPlayers(){
		putByte(Packet.NAK_NOT_ENOUGH_PLAYERS);
	}
	
	public byte getNAKID(){
		return getByte();
	}
	
}