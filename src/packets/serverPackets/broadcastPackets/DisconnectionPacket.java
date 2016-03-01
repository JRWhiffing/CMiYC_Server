package packets.serverPackets.broadcastPackets;

import packets.Packet;

public class DisconnectionPacket extends Packet {
	
	public DisconnectionPacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_QUIT);
	}
	
	public DisconnectionPacket(byte[] data){
		packet = data;
	}
	
	public void putPlayerID(int playerID){
		putInt(playerID);
	}
	
	//Have seperate function for each reason or just have byte passed to packet?
	public void putReasonQuit(){
		putByte(Packet.DISCONNECT_QUIT);
	}
	
	public void putReasonPoorConnection(){
		putByte(Packet.DISCONNECT_POOR_CONNECTION);
	}
	
	public void putReasonKick(){
		putByte(Packet.DISCONNECT_KICK);
	}
	
	public int getPlayerID(){
		return getInt();
	}
	
	public byte getDisconnectReason(){
		return getByte();
	}

}