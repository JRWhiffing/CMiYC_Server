package server.packets;

public class ClientPacket extends Packet {

	public ClientPacket(){//Create new packet
		super.putByte(ClientPacket.CLIENT_SENDER_ID);
	}
	
	public ClientPacket(byte[] clientPacket){//Create packet from existing data.
		super.packet = clientPacket;
	}
	
	//ID Bytes
	public static final byte CLIENT_SENDER_ID = 0x02;
	public static final byte LOCATION_ID = 0x01;
	public static final byte PING_RESPONSE = 0x02;
	public static final byte CATCH_PERFORMED = 0x03;
	public static final byte CAPTURED = 0x04;
	public static final byte ABILITY_USAGE = 0x05;
	public static final byte VOTE = 0x06;
	public static final byte REPORT = 0x07;
	public static final byte QUIT = 0x08;
	public static final byte JOIN = 0x09;
	public static final byte HOST_ACTION = 0x0A;
	public static final byte ACK = 0x0B;
	public static final byte NAK = 0x0C;
	public static final byte BAD_SPAWN = 0x0D;
	public static final byte PLAYER_READY = 0x0E;
	
	//Host Action IDs
	public static final byte HOST_ACTION_START_ROUND = 0x01;
	public static final byte HOST_ACTION_END_ROUND = 0x02;
	public static final byte HOST_ACTION_KICK_PLAYER = 0x03;
	public static final byte HOST_ACTION_CREATE_ROOM = 0x04;
	public static final byte HOST_ACTION_CLOSE_ROOM = 0x05;
	public static final byte HOST_ACTION_TIME_LIMIT = 0x06;
	public static final byte HOST_ACTION_SCORE_LIMIT = 0x07;
	public static final byte HOST_ACTION_CHANGE_GAMEMODE = 0x08;
	public static final byte HOST_ACTION_SET_BOUNDARIES = 0x09;
	public static final byte HOST_ACTION_BOUNDARY_UPDATES = 0x0A;
	public static final byte HOST_ACTION_CHANGE_HOST = 0x0B;
	public static final byte HOST_ACTION_ALLOW_VOTING = 0x0C;
	
}