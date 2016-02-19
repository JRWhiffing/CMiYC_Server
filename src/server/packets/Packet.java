package server.packets;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class Packet {//will need to test for construction of packets, worked however when testing individually
	
	protected byte[] packet = new byte[0];
	
	public byte getByte(){
		byte byte_ = packet[0];
		packet = Arrays.copyOfRange(packet, 1, packet.length);
		
		//printPacket();
		
		return byte_;
	}
	
	public void putByte(byte byte_){
		packet = Arrays.copyOf(packet, packet.length + 1);
		packet[packet.length - 1] = byte_;
		
		//printPacket();
	}
	
	public char getChar(){
		char character;
		byte[] character_ = new byte[]{packet[0], packet[1]};
		character = ByteBuffer.wrap(character_).getChar();
		packet = Arrays.copyOfRange(packet, 2, packet.length);
		
		//printPacket();
		
		return character;
	}
	
	public void putChar(char character){
		byte[] character_ = new byte[2];
		ByteBuffer.wrap(character_).putChar(character);
		packet = Arrays.copyOf(packet, packet.length + 2);
		packet[packet.length - 1] = character_[0];
		packet[packet.length] = character_[1];
		
		//printPacket();
	}
	
	public double getDouble(){
		double d_;
		byte[] double_;
		
		double_ = Arrays.copyOf(packet, 8);
		d_ = ByteBuffer.wrap(double_).getDouble();
		packet = Arrays.copyOfRange(packet, 8, packet.length);
		
		//printPacket();
		
		return d_;
	}
	
	public void putDouble(double d_){
		byte[] double_ = new byte[8];
		ByteBuffer.wrap(double_).putDouble(d_);
		
		packet = Arrays.copyOf(packet, packet.length + 8);
		
		for(int i = 0; i < 8; i ++){
			packet[packet.length - (8-i)] = double_[i];
		}
		
		//printPacket();
	}
	
	public void putString(String string, int length){//need to include padding in the event the string is not of a preset size.
		byte[] string_ = string.getBytes();
		for(int i = 0; i < string_.length; i++){
			putByte(string_[i]);
		}
		while(string_.length < (length * 2)){
			putByte((byte) 0x00);//2 bytes is a char 0x00, 0x7C is '|'
			putByte((byte) 0x7C);
		}
	}
	
	public String getString(int length){
		String string_ = "";
		char temp;
		for(int i = 0; i < length; i++){
			temp = getChar();
			if(temp != '|'){//removing padding
				string_ += temp;
			}
		}
		
		return string_;
	}
	
	public void putRoomKey(String key){
		putString(key, 5);
	}
	
	public String getRoomKey(){
		return getString(5);//5 Digit base 36 (alphabet and numbers) code?
	}
	
	public void putName(String name){
		putString(name, 15);
	}
	
	public String getName(){
		return getString(15);//character limit of 15?
	}
	
	public byte[] getPacket(){//used to perform a checksum on a packet and return it.
		byte[] newPacket = new byte[packet.length + 1];
		byte checksum = 0x00;
		
		for(int i = 0; i < packet.length; i++){
			checksum += packet[i];
			newPacket[i] = packet[i];
		}
		newPacket[newPacket.length - 1] = checksum; //adding the checksum to the packet.
		
		printPacket();
		
		return newPacket;
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
}