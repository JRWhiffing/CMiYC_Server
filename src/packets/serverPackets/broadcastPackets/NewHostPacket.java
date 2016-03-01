package packets.serverPackets.broadcastPackets;

import packets.Packet;

public class NewHostPacket extends Packet {

	public NewHostPacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_NEW_HOST);
	}
	
	public NewHostPacket(byte[] data){
		packet = data;
	}
	
	public void putPlayerID(int playerID){//the id of the new host
		putInt(playerID);
	}
	
	public int getPlayerID(){
		return getInt();
	}
	
}