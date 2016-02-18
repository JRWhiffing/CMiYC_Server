package server.packets;


public class ServerPacket {

	byte[] packet = new byte[0];
	// write data to byte buffer 
	
	ServerPacket(){
		
	}
	
	
	
	
	public byte[] getChecksum(byte[] packet){//used to perform a checksum on a packet.
		byte[] newPacket = new byte[packet.length + 1];
		byte checksum = 0x00;
		
		for(int i = 0; i < packet.length; i++){
			checksum += packet[i];
			newPacket[i] = packet[i];//could replace with an Array copy thingy.
		}
		newPacket[newPacket.length - 1] = checksum; //adding the checksum to the packet.
		
		return newPacket;
	}
	
	public static final byte ServerSenderID = 0x01;
	public static final byte NAK = 0x0E;
	public static final byte NAK0x01 = 0x01;
	public static final byte NAK0x02 = 0x02;
	public static final byte NAK0x03 = 0x03;
	
}
