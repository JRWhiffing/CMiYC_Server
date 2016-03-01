package packets.serverPackets.broadcastPackets;

import packets.Packet;

public class TimeRemainingPacket extends Packet {

	public TimeRemainingPacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_TIME_REMAINING);
	}
	
	public TimeRemainingPacket(byte[] data){
		packet = data;
	}
	
	public void putTimeReaming(int minutes, int seconds){
		putInt(minutes);
		putInt(seconds);
	}
	
	public int[] getTimeRemaining(){
		return new int[]{getInt(), getInt()};
	}
	
}