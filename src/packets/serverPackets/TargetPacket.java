package packets.serverPackets;

import packets.Packet;

public class TargetPacket extends Packet {

	public TargetPacket(){
		putByte(Packet.TARGET);
	}
	
	public TargetPacket(byte[] data){
		packet = data;
	}
	
	public void putTargetID(int targetID){
		putInt(targetID);
	}
	
	public int getTargetID(){
		return getInt();
	}
	
}