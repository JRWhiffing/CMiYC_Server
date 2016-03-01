package packets.serverPackets.broadcastPackets;

import packets.Packet;

public class CapturePacket extends Packet {

	public CapturePacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_CAPTURE);
	}
	
	public CapturePacket(byte[] data){
		packet = data;
	}
	
	public void putCapture(int targetID, int pursuerID){
		putInt(targetID);
		putInt(pursuerID);
	}
	
	public int[] getCapture(){
		return new int[]{getInt(), getInt()};
	}
	
}