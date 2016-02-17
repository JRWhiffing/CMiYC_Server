package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import server.packets.*;
import server.packets.broadcast.*;
import server.packets.lobbyinfo.*;

public class StateMachine {

	StateMachine(){
		
	}
	
	//Must have method for each protocol, either hear or from across System.
	
	public byte[] /*Packet*/ stateMachine(byte[] packet){//not sure on best way to pass data to StateMachine
		byte[] clientPacket;
		Scanner packetReader;
		byte dataID;
		if(packet[0] == 1){//check that the checksum passed.
			clientPacket = Arrays.copyOfRange(packet, 1, packet.length - 1);//need to check that -1 is correct, might just not need it.
			packetReader = new Scanner(Arrays.toString(packet));
			dataID = packetReader.nextByte();
		} else {
			dataID = 0x00; //if the checksum failed set the id to 0x00 as this will be handled by the default case.
		}
		
		byte[] serverPacket = new byte[]{0};
		switch (dataID){//each case is a protocol.
		
		case 0x01 :
			
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
	
}
