package server.packets.clientPackets;

import server.packets.Packet;

public class ReportPacket extends Packet {

	public ReportPacket(){
		putByte(Packet.REPORT);
	}
	
	public ReportPacket(byte[] data){
		packet = data;
	}
	
	public void report(int playerID){
		putInt(playerID);
	}
	
	public int getReport(){
		return getInt();
	}
	
}