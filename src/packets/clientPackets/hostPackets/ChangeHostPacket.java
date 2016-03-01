package packets.clientPackets.hostPackets;

import packets.Packet;

public class ChangeHostPacket extends Packet {
	
	public ChangeHostPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_CHANGE_HOST);
	}
	
	public ChangeHostPacket(byte[] data){
		packet = data;
	}
	
	public void putNewHostID(int hostID){
		putInt(hostID);
	}
	
	public int getHostID(){
		return getInt();
	}
	
}