package server;

import server.packets.*;

public class StateMachine {
	
	private final int clientID;
	private ClientPacket c_Packet;
	protected ServerPacket s_Packet;
	
	StateMachine(Integer clientID){
		//can be used in the event of knowing which thread to close when disconnecting.
		this.clientID = clientID;
	}
	
	//Must have method for each protocol, either hear or from across System.
	
	public void stateMachine(byte[] packet){//not sure on best way to pass data to StateMachine
		s_Packet = new ServerPacket();
		c_Packet = new ClientPacket(packet);
		byte dataID;
		if(c_Packet.getByte() == 0x01){//check that the checksum passed.
			c_Packet.getByte();//getting rid of the senderID
			dataID = c_Packet.getByte();
		} else {
			dataID = 0x00; //if the checksum failed set the id to 0x00 as this will be handled by the default case.
		}
		
		switch (dataID){//each case is a protocol.
		
		case 0x01 : //Location
			double[] location = new double[2];
			location[0] = c_Packet.getDouble();
			location[1] = c_Packet.getDouble();
			//testing response
			s_Packet.putByte(ServerPacket.LOCATION);
			s_Packet.putDouble(location[0]);
			s_Packet.putDouble(location[1]);
			sendPacket();
			break;
			
		case 0x02 : //Ping Response
			//Server needs to send back ping response
			break;
			
		case 0x03 : //Catch Performed	
			//Server needs to complete some kind of action to check if player has been caught
			//If Player has been caught successfully then response needs to be given
			break;
			
		case 0x04 : //Captured by pursuer
			//Server will need to check if action is correct. Leaderboard and player instances will be updated
			//Server will need to respond appropiately.
			break;
			
		case 0x05 : //Ability Usage
			int[] ability = new int[1]; //Ability Data Type might need changing?
			//Some sort of ability manipulation needed here
			//Server may need response if ability changes an element of the game
			break;
			
		case 0x06 : //Vote in a Lobby
			int[] gameMode = new int[1]; //Gamemode Data Type change again?
			//Server sends a response updating vote count
			break;
			
		case 0x07 : //Report?
			//??
			break;
			
		case 0x08 : //Client quits
			//Server removes player instance from the game
			//Server sends broadcast to all clients notifying that a player has left
			break;
		
		case 0x09 : //Client joins
			int[] roomKey = new int[6]; //Room Key
			double[] macAddress = new double[10]; //Mac Address
			//Server checks room key and completes some kind of action
			break;
			
		case 0x0A : //Action completed by a host
			int[] hostID = new int[2]; //Host Action ID
			//Seperate Case statement will be needed for this
			//Server will complete an action depending on the ID
			break;
			
		case 0x0B : //NAK Resend packet
			//Server will resend packet to client
			break;
			
		case 0x0C : //ACK Client Acknowledgement of last packet
			//Confirmation that packet was sent
			//No response required by server
			break;
			
		case 0x0D : //Bad Spawn - MAY NOT BE NEEDED ANYMORE IF WE ARENT IMPLEMENTING SPAWN
			//Server will provide a new spawn
			break;
			
		case 0x0E : //Player has reached their spawn - AGAIN MAY NOT BE NEEDED
			//Game starts..
			break;
			
			
		default : //The NAK response to requesting that the last packet be resent.
			System.err.println("Unrecognized Data ID");
			s_Packet.putByte(ServerPacket.NAK);
			s_Packet.putByte(ServerPacket.NAK_RESEND);
			sendPacket();
			break;
		}
	}
	
	private void sendPacket(){
		System.out.println("Returning Packet");
		Server.sendPacket(clientID, s_Packet.getPacket());
	}
	
}