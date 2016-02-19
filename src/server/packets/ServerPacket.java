package server.packets;


public class ServerPacket extends Packet {

	// write data to byte buffer 
	
	public ServerPacket(){
		super.putByte(ServerPacket.SERVER_SENDER_ID);
	}
	
	public ServerPacket(byte[] serverPacket){
		super.packet = serverPacket;
	}
	
	//ID Bytes
	public static final byte SERVER_SENDER_ID = 0x01;
	public static final byte LOCATION = 0x01;
	public static final byte PING = 0x02;
	
	
	public static final byte NAK = 0x0E;	
	
}