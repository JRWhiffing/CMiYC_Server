package packets.serverPackets;

import packets.Packet;

public class TargetPacket extends Packet {

	public TargetPacket(){
		putByte(Packet.TARGET);
	}
	
	public TargetPacket(byte[] data){
		packet = data;
	}
	
	public void putTargetID(int[] targetID){
		int integer = targetID.length;
		putInt(1);
		for(int i = 0; i < targetID.length; i++){
			putInt(targetID[i]);
		}
	}
	
	public int[] getTargetID(){
		int[] target = new int[getInt()];
		for(int i = 0; i < target.length; i++){
			target[i] = getInt();
		}
		return target;
	}
	
}