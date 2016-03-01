package packets.clientPackets.hostPackets;

import packets.Packet;

public class GameTypePacket extends Packet {
	
	public GameTypePacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_CHANGE_GAMETYPE);
	}
	
	public GameTypePacket(byte[] data){
		packet = data;
	}
	
	public void putGameType(byte gameType){
		putByte(gameType);
	}
	
	public byte getGameType(){
		return getByte();
	}

}