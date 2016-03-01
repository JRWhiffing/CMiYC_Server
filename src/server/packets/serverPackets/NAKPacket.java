package server.packets.serverPackets;

import server.packets.Packet;

public class NAKPacket extends Packet {
	
	public NAKPacket(){
		super.putByte(Packet.SERVER_NAK);
	}
	
	public NAKPacket(byte[] data){
		super.packet = data;
	}
	
	public void invalidRoomKey(){
		super.putByte(Packet.NAK_INVALID_ROOM_KEY);
	}
	
	public void notEnoughPlayers(){
		super.putByte(Packet.NAK_NOT_ENOUGH_PLAYERS);
	}
	
	public byte getNAKID(){
		return super.getByte();
	}
	
}