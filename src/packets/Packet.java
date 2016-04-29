package packets;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * An abstract class that contains packet building and unpacking functionality
 * Allows for byte, integer, double, character and String data type usage in Packets
 * @authors James and Adam
 *
 */
public abstract class Packet {
	
	protected byte[] packet = new byte[0]; //Storage for packet data
	
	/**
	 * Method that extracts a byte of data from the front of the packet array
	 * @return - The byte of data extracted
	 */
	protected byte getByte() {
		byte byte_ = packet[0];
		//Reduces the Packet Array size by 1
		packet = Arrays.copyOfRange(packet, 1, packet.length);
		
		//printPacket();
		
		return byte_;
	}
	
	/**
	 * Method that puts a byte of data into the packet array
	 * @param byte_ - Byte of data that is put into the packet array
	 */
	protected void putByte(byte byte_) {
		//Increases the Packet Array by 1
		packet = Arrays.copyOf(packet, packet.length + 1);
		packet[packet.length - 1] = byte_;
		
		//printPacket();
	}
	
	/**
	 * Method that extracts a type char of data from the front of the packet array
	 * @return - The type char of data extracted
	 */
	protected char getChar() {
		char character;
		//Char Data Type occupies two bytes of space
		byte[] character_ = new byte[]{packet[0], packet[1]};
		character = ByteBuffer.wrap(character_).getChar();
		packet = Arrays.copyOfRange(packet, 2, packet.length);
		
		//printPacket();
		
		return character;
	}
	
	/**
	 * Method that puts a type char of data into the packet array
	 * @param character - Type char of data that is put into the packet array
	 */
	protected void putChar(char character) {
		//Char Data Type occupies two bytes of space
		byte[] character_ = new byte[2];
		ByteBuffer.wrap(character_).putChar(character);
		packet = Arrays.copyOf(packet, packet.length + 2);
		packet[packet.length - 2] = character_[0];
		packet[packet.length - 1] = character_[1];
		
		//printPacket();
	}
	
	/**
	 * Method that extracts a type double of data from the front of the packet array
	 * @return - The type double of data extracted
	 */
	protected double getDouble() {
		double d_;
		byte[] double_;
		//Double Data Type occupies eight bytes of space
		double_ = Arrays.copyOf(packet, 8);
		d_ = ByteBuffer.wrap(double_).getDouble();
		packet = Arrays.copyOfRange(packet, 8, packet.length);
		//printPacket();
		
		return d_;
	}
	
	/**
	 * Method that puts a type double of data into the packet array
	 * @param d_ - Type double of data that is put into the packet array
	 */
	protected void putDouble(double d_) {
		//Double Data Type occupies eight bytes of space
		byte[] double_ = ByteBuffer.allocate(8).putDouble(d_).array();
		
		packet = Arrays.copyOf(packet, packet.length + 8);
		
		for (int i = 0; i < 8; i++) {
			packet[packet.length - (8 - i)] = double_[i];
		}
		
		//printPacket();
	}
	
	/**
	 * Method that extracts a type int of data from the front of the packet array
	 * @return - The type int of data extracted
	 */
	protected int getInt() {
		byte[] int_;
		int integer;
		//Int Data Type occupies four bytes of space
		int_ = Arrays.copyOf(packet, 4);
		integer = ByteBuffer.wrap(int_).getInt();
		packet = Arrays.copyOfRange(packet, 4, packet.length);
		
		//printPacket();
		
		return integer;
	}
	
	public void appendlength(){
		int length = packet.length;
		byte[] oldPacket = packet;
		packet = new byte[0];
		putInt(length);
		byte[] newPacket = new byte[packet.length + oldPacket.length];
		System.arraycopy(packet, 0, newPacket, 0, packet.length);
		System.arraycopy(oldPacket, 0, newPacket, packet.length, oldPacket.length);
		packet = newPacket;
		if(packet[4] != 1){
			System.out.println("Packet: size-"+length+" contents-["+toString()+"]");
		}
	}
	
	/**
	 * Method that puts a type int of data into the packet array
	 * @param integer - Type int of data that is put into the packet array
	 */
	protected void putInt(int integer) {
		//Int Data Type occupies four bytes of space
		byte[] int_ = ByteBuffer.allocate(4).putInt(integer).array();
		
		packet = Arrays.copyOf(packet, packet.length + 4);
		
		for(int i = 0; i < 4; i ++){
			packet[packet.length - (4 - i)] = int_[i];
		}
		
		//printPacket();
	}
	
	/**
	 * Method that extracts a type String of data from the front of the packet array
	 * @return - The type String of data extracted
	 */
	protected String getString() {
		String string_ = "";
		//Extracts the length of the String to know how much to read
		int length = getInt();
		for (int i = 0; i < length; i++) {
			string_ += getChar();
		}
		return string_;
	}
	
	/**
	 * Method that puts a type String of data into the packet array
	 * @param string - Type String of data that is put into the packet array
	 */
	//Need to include padding in the event the string is not of a preset size.
	protected void putString(String string) {
		char[] string_ = string.toCharArray();
		int length =  string.length();
		//Puts the length of the String into the packet first
		putInt(length);
		for (int i = 0; i < length; i++) {
			putChar(string_[i]);
		}
	}
	
	//May not be needed anymore as TCP/IP deals with data loss checking
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
	
	/**
	 * Method that converts the whole packet's data into a String
	 */
	public String toString() {
		String bytes = " | ";
		//Puts a bar after each array element which is helpful in differentiating between bytes of data
		for (int i = 0; i < packet.length; i++) {
			bytes += packet[i] + " | ";
		}
		return bytes;
	}

	/**
	 * Method that prints all the packet's data as a String to the screen
	 */
	private void printPacket(){
		System.out.println(toString());
	}
	
	public int size(){
		return packet.length;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * PACKET DATA IDs
	 */
	
	//Ability IDs
	
	//Hides the player location from their pursuer
	public static final byte ABILITY_HIDE = 0X01;
	//Produces beeps on target's device, i.e. if nearby you can hear the beep and find the target
	public static final byte ABILITY_PING = 0X02;
	//Sends a fake location to the pursuer for x amount of time or until pursuer enters within y meters of decoy
	public static final byte ABILITY_DECOY = 0X03;
	//Target not alerted when pursuer is within x meters of target
	public static final byte ABILITY_SNEAK = 0X04;
	
	//Gametype IDs
	public static final byte GAMETYPE_DEFAULT = 0X01;
	public static final byte GAMETYPE_TEAM = 0X02;
	public static final byte GAMETYPE_MAN_HUNT = 0X03;
	
	//NAK IDs
	public static final byte NAK_INVALID_ROOM_KEY = 0x01;
	public static final byte NAK_NOT_ENOUGH_PLAYERS = 0x02;
	public static final byte NAK_ROOM_FULL = 0x03;
	public static final byte NAK_NO_VALID_TARGETS = 0x04;
	public static final byte NAK_ROOM_IN_GAME = 0x05;
	
	//Disconnect IDs
	public static final byte DISCONNECT_QUIT = 0X01;
	public static final byte DISCONNECT_POOR_CONNECTION = 0X02;
	public static final byte DISCONNECT_KICK = 0X03;
	
	//Votes IDs
	public static final byte VOTES_ENABLED= 0x01;
	public static final byte VOTES_DISABLED= 0x02;
	
	//Kick IDs
	public static final byte KICK_POOR_CONNECTION = 0X01;
	public static final byte KICK_KICKED = 0X02;
	
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
	public static final byte ACK = 0x0B;
	public static final byte BAD_SPAWN = 0x0C;
	public static final byte PLAYER_READY = 0x0D;
	
	//Client Host Action IDs
	public static final byte HOST_ACTION_START_ROUND = 0x01;
	public static final byte HOST_ACTION_END_ROUND = 0x02;
	public static final byte HOST_ACTION_KICK_PLAYER = 0x03;
	public static final byte HOST_ACTION_CREATE_ROOM = 0x04;
	public static final byte HOST_ACTION_CLOSE_ROOM = 0x05;
	public static final byte HOST_ACTION_TIME_LIMIT = 0x06;
	public static final byte HOST_ACTION_SCORE_LIMIT = 0x07;
	public static final byte HOST_ACTION_CHANGE_GAMETYPE = 0x08;
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
	public static final byte NAK = 0x0D;
	public static final byte HOST = 0x0E;
	public static final byte CAUGHT = 0x0F;
	public static final byte JOIN_SUCCESS = 0x10;
	public static final byte CATCH_SUCCESS = 0x11;
		
	//Broadcast IDs
	public static final byte BROADCAST_TIME_REMAINING = 0x01;
	public static final byte BROADCAST_LEADERBOARD = 0x02;
	public static final byte BROADCAST_CAPTURE = 0x03;
	public static final byte BROADCAST_VOTES = 0x04;
	public static final byte BROADCAST_QUIT = 0x05;
	public static final byte BROADCAST_BOUNDARY_UPDATE = 0x06;
	public static final byte BROADCAST_NEW_HOST = 0x07;
	public static final byte BROADCAST_NEW_PLAYER = 0x08;
		
	//Lobby Information IDs
	public static final byte LOBBYINFO_GAMETYPE = 0x01;
	public static final byte LOBBYINFO_TIME_LIMIT = 0x02;
	public static final byte LOBBYINFO_SCORE_LIMIT = 0x03;
	public static final byte LOBBYINFO_BOUNDARIES = 0x04;
	public static final byte LOBBYINFO_LEADERBOARD = 0x05;
	public static final byte LOBBYINFO_ROOM_NAME = 0x06;
	public static final byte LOBBYINFO_VOTES = 0x07;
	public static final byte LOBBYINFO_BOUNDARY_UPDATES = 0x08;
}