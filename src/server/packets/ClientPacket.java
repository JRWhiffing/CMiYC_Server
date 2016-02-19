package server.packets;

public class ClientPacket extends Packet {

	//read from byte buffer
	
	public ClientPacket(){
		super.putByte(ClientPacket.CLIENT_SENDER_ID);
	}
	
	public ClientPacket(byte[] clientPacket){
		super.packet = clientPacket;
	}
	
	
	//ID Bytes
	public static final byte CLIENT_SENDER_ID = 0x02;
	public static final byte LOCATION_ID = 0x01;
	public static final byte PING_RESPONSE = 0x02;
	public static final byte CATCH_PERFORMED = 0X03;
	public static final byte CAPTURED = 0X04;
	
	public static final byte ABILITY_USAGE = 0X05;
	
	public static final byte VOTE = 0X06;
	public static final byte REPORT = 0X07;
	public static final byte QUIT = 0X08;
	public static final byte JOIN = 0X09;
	
	public static final byte HOST_ACTION = 0X0A;
	public static final byte HOST_ACTION_START_ROUND = 0X01;
	
	
}