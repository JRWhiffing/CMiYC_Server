package server;

import java.util.Arrays;
import java.util.Scanner;

import server.packets.*;
import server.packets.broadcast.*;
import server.packets.lobbyinfo.*;

public class StateMachine {
	
	private final Integer clientID;	
	private Scanner packetReader;
	
	StateMachine(Integer clientID){
		//can be used in the event of knowing which thread to close when disconnecting.
		this.clientID = clientID;
	}
	
	//Must have method for each protocol, either hear or from across System.
	
	public byte[] /*Packet*/ stateMachine(byte[] packet){//not sure on best way to pass data to StateMachine
		byte[] clientPacket;
		byte dataID;
		if(packet[0] == 1){//check that the checksum passed.
			clientPacket = Arrays.copyOfRange(packet, 1, packet.length - 1);//need to check that -1 is correct, might just not need it.
			packetReader = new Scanner(Arrays.toString(packet));
			System.out.print(packetReader.nextByte());//getting rid of the senderID
			dataID = packetReader.nextByte();
			System.out.print(" | " + dataID);
		} else {
			dataID = 0x00; //if the checksum failed set the id to 0x00 as this will be handled by the default case.
		}
		
		byte[] serverPacket = new byte[]{0};
		switch (dataID){//each case is a protocol.
		
		case 0x01 :
			String text = "";
			for(int i = 0; i < 5; i++){
				text += readChar();
			}
			break;
			
		default : //The NAK response to requesting that the last packet be resent.
			System.err.println("Unrecognized Data ID");
			serverPacket = NAK.NAK0x01;
			serverPacket = getChecksum(serverPacket);
			break;
		}
		return serverPacket;
	}
	
	private byte[] getChecksum(byte[] packet){//used to perform a checksum on a packet.
		byte[] newPacket = new byte[packet.length + 1];
		byte checksum = 0x00;
		
		for(int i = 0; i < packet.length; i++){
			checksum += packet[i];
			newPacket[i] = packet[i];//could replace with an Array copy thingy.
		}
		newPacket[newPacket.length - 1] = checksum; //adding the checksum to the packet.
		
		return new byte[0];
	}
	
	private char readChar(){//will read a character from the packet.
		char character;
		byte[] character_ = new byte[2];
		character_[0] = packetReader.nextByte();
		character_[1] = packetReader.nextByte();
		//shifting the first byte by 8 bits and then masking with the second byte.
		character = ((char) ((character_[0] << 8) | (character_[1] & 0xFF)));
		
		return character;
	}
	
}