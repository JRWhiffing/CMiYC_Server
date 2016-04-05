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
	
	public void putDisconnectionReason(byte reason) {
		putByte(reason);
	}
	
	public int getPlayerID(){
		return getInt();
	}
	
	public byte getDisconnectReason(){
		return getByte();
	}

}