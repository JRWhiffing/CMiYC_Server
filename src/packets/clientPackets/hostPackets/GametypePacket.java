package packets.clientPackets.hostPackets;

import packets.Packet;

public class GametypePacket extends Packet {
	
	public GametypePacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_CHANGE_GAMETYPE);
	}
	
	public GametypePacket(byte[] data){
		packet = data;
	}
	
	public void putGameType(byte gameType){
		putByte(gameType);
	}
	
	public byte getGameType(){
		return getByte();
	}

}