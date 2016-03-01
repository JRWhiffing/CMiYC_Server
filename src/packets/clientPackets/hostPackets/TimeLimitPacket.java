package packets.clientPackets.hostPackets;

import packets.Packet;

public class TimeLimitPacket extends Packet {

	public TimeLimitPacket(){
		putByte(Packet.HOST_ACTION);
		putByte(Packet.HOST_ACTION_TIME_LIMIT);
	}
	
	public TimeLimitPacket(byte[] data){
		packet = data;
	}
	
	public void putTimeLimit(int time){//time in minutes
		putInt(time);
	}
	
	public int getTimeLimit(){
		return getInt();
	}
	
}