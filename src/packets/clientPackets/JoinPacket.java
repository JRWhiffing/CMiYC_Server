package packets.clientPackets;

import packets.Packet;

public class JoinPacket extends Packet {

	public JoinPacket(){
		putByte(Packet.JOIN);
	}
	
	public JoinPacket(byte[] data){
		packet = data;
	}
	
	public void putRoomKey(String key){
		putString(key);
	}
	
	public void putMACAddress(double[] address){
		for(int i = 0; i < 6; i++){
			putDouble(address[i]);
		}
	}
	
	public String getRoomKey(){
		return getString();
	}
	
	public double[] getMACAddress(){
		double[] address = new double[6];
		for(int i = 0; i < 6; i++){
			address[i] = getDouble();
		}
		return address;
	}
	
}