package server.packets;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class Packet {//will need to test for construction of packets, worked however when testing individually
	
	protected byte[] packet = new byte[0];
	
	protected byte getByte(){
		byte byte_ = packet[0];
		packet = Arrays.copyOfRange(packet, 1, packet.length);
		
		//printPacket();
		
		return byte_;
	}
	
	protected void putByte(byte byte_){
		packet = Arrays.copyOf(packet, packet.length + 1);
		packet[packet.length - 1] = byte_;
		
		//printPacket();
	}
	
	protected char getChar(){
		char character;
		byte[] character_ = new byte[]{packet[0], packet[1]};
		character = ByteBuffer.wrap(character_).getChar();
		packet = Arrays.copyOfRange(packet, 2, packet.length);
		
		//printPacket();
		
		return character;
	}
	
	protected void putChar(char character){
		byte[] character_ = new byte[2];
		ByteBuffer.wrap(character_).putChar(character);
		packet = Arrays.copyOf(packet, packet.length + 2);
		packet[packet.length - 2] = character_[0];
		packet[packet.length - 1] = character_[1];
		
		//printPacket();
	}
	
	protected double getDouble(){
		double d_;
		byte[] double_;
		
		double_ = Arrays.copyOf(packet, 8);
		d_ = ByteBuffer.wrap(double_).getDouble();
		packet = Arrays.copyOfRange(packet, 8, packet.length);
		
		//printPacket();
		
		return d_;
	}
	
	protected void putDouble(double d_){
		byte[] double_ = new byte[8];
		ByteBuffer.wrap(double_).putDouble(d_);
		
		packet = Arrays.copyOf(packet, packet.length + 8);
		
		for(int i = 0; i < 8; i ++){
			packet[packet.length - (8-i)] = double_[i];
		}
		
		//printPacket();
	}
	
	protected int getInt(){
		byte[] int_;
		int integer;
		int_ = Arrays.copyOf(packet, 4);
		integer = ByteBuffer.wrap(int_).getInt();
		packet = Arrays.copyOfRange(packet, 4, packet.length);
		
		//printPacket();
		
		return integer;
	}
	
	protected void putInt(int integer){
		byte[] int_ = new byte[4];
		ByteBuffer.wrap(int_).putDouble(integer);
		
		packet = Arrays.copyOf(packet, packet.length + 4);
		
		for(int i = 0; i < 4; i ++){
			packet[packet.length - (4-i)] = int_[i];
		}
		
		//printPacket();
	}
	
	protected void putString(String string){//need to include padding in the event the string is not of a preset size.
		char[] string_ = string.toCharArray();
		int length =  string.length();
		putInt(length);//putting the length of the string first
		for(int i = 0; i < length; i++){
			putChar(string_[i]);
		}
	}
	
	protected String getString(){
		String string_ = "";
		int length = getInt();//getting the length of the string to know how much to read
		for(int i = 0; i < length; i++){
			string_ += getChar();
		}
		
		return string_;
	}
	
	public byte[] getPacket(){//used to perform a checksum on a packet and return it.
//		byte[] newPacket = new byte[packet.length + 1];
//		byte checksum = 0x00;
//		
//		for(int i = 0; i < packet.length; i++){
//			checksum += packet[i];
//			newPacket[i] = packet[i];
//		}
//		newPacket[newPacket.length - 1] = checksum; //adding the checksum to the packet.
//		
//		System.out.println(toString() + checksum + " | ");
//		
//		return newPacket;
		return packet;
	}
	
	@Override
	public String toString(){
		String bytes = " | ";
		for(int i = 0; i < packet.length; i ++){
			bytes += packet[i] + " | ";
		}
		return bytes;
	}

	private void printPacket(){
		System.out.println(toString());
	}
	
	//Data IDs
	//Ability IDs
	public static final byte ABILITY_HIDE = 0X01;//hide the player location from their pursuer
	public static final byte ABILITY_PING = 0X02;//produce beep on target device, i.e. if nearby you can hear the beep and find the target
	public static final byte ABILITY_DECOY = 0X03;//send a fake location to the pursuer for x amount of time or until pursuer enters within y meters of decoy
	public static final byte ABILITY_SNEAK = 0X04;//target not alerted when pursuer is within x meters of target
	
	//Gametype IDs
	public static final byte GAMETYPE_DEFAULT = 0X01;
	public static final byte GAMETYPE_TEAM = 0X02;
	public static final byte GAMETYPE_MAN_HUNT = 0X03;
	
	//NAK IDs
	public static final byte NAK_RESEND = 0x01;
	public static final byte NAK_INVALID_ROOM_KEY = 0x02;
	public static final byte NAK_NOT_ENOUGH_PLAYERS = 0x03;
	
	//Disconnect IDs
	public static final byte DISCONNECT_QUIT = 0X01;
	public static final byte DISCONNECT_POOR_CONNECTION = 0X02;
	public static final byte DISCONNECT_KICK = 0X03;
	
	//Kick IDs
	public static final byte KICK_POOR_CONNECTION = 0X01;
	public static final byte KICK_KICKED = 0X02;
	
	//Votes Toggle IDs
	public static final byte VOTES_ENABLED = 0X01;
	public static final byte VOTES_DISABLED = 0X01;
	
	//Location ID -- same for server and client
	public static final byte LOCATION = 0x01;
	
	/*----------------------------------------------------------*/
	
	//Client IDs
	public static final byte PING_RESPONSE = 0x02;
	public static final byte CATCH_PERFORMED = 0x03;
	public static final byte CAPTURED = 0x04;
	public static final byte ABILITY_USAGE = 0x05;
	public static final byte VOTE = 0x06;
	public static final byte REPORT = 0x07;
	public static final byte QUIT = 0x08;
	public static final byte JOIN = 0x09;
	public static final byte HOST_ACTION = 0x0A;
	public static final byte CLIENT_ACK = 0x0B;
	public static final byte CLIENT_NAK = 0x0C;
	public static final byte BAD_SPAWN = 0x0D;
	public static final byte PLAYER_READY = 0x0E;
	
	//Client Host Action IDs
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
	
	/*----------------------------------------------------------*/
	
	//Server IDs
	public static final byte PING = 0x02;
	public static final byte BROADCAST = 0x03;
	public static final byte TARGET = 0x04;
	public static final byte SPAWN_REGION = 0x05;
	public static final byte ABILITY_ACTION = 0x06;
	public static final byte GAME_START = 0x07;
	public static final byte GAME_END = 0x08;
	public static final byte ROOM_CLOSE = 0x09;
	public static final byte ROOM_KEY = 0x0A;
	public static final byte LOBBYINFO = 0x0B;
	public static final byte KICK = 0x0C;
	public static final byte SERVER_ACK = 0x0D;
	public static final byte SERVER_NAK = 0x0E;
	public static final byte HOST = 0x0F;
		
		//Broadcast IDs
		public static final byte BROADCAST_TIME_REMAINING = 0x01;
		public static final byte BROADCAST_LEADERBOARD = 0x02;
		public static final byte BROADCAST_CAPTURE = 0x03;
		public static final byte BROADCAST_VOTES = 0x04;
		public static final byte BROADCAST_QUIT = 0x05;
		public static final byte BROADCAST_BOUNDARY_UPDATE = 0x06;
		public static final byte BROADCAST_NEW_HOST = 0x07;
		public static final byte BROADCAST_NEW_PLAYER = 0x08;
		public static final byte BROADCAST_PLAYER_READY = 0x09;
		
		//Lobby Information IDs
		public static final byte LOBBYINFO_GAMETYPE = 0x01;
		public static final byte LOBBYINFO_TIME_LIMIT = 0x02;
		public static final byte LOBBYINFO_SCORE_LIMIT = 0x03;
		public static final byte LOBBYINFO_BOUNDARIES = 0x04;
		public static final byte LOBBYINFO_LEADERBOARD = 0x05;
		public static final byte LOBBYINFO_ROOM_NAME = 0x06;
		public static final byte LOBBYINFO_VOTES = 0x07;
}