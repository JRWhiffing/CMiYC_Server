package server;

import java.util.Arrays;

import server.packets.*;
import server.packets.serverPackets.*;
import server.packets.clientPackets.*;

public class StateMachine {
	
	private final int clientID;
	private Packet serverPacket;
	private Packet clientPacket;
	
	StateMachine(Integer clientID){
		//can be used in the event of knowing which thread to close when disconnecting.
		this.clientID = clientID;
	}
	
	//Must have method for each protocol, either hear or from across System.
	
	public void stateMachine(byte[] packet){//not sure on best way to pass data to StateMachine
		byte dataID = packet[0];
		
		switch (dataID){//each case is a protocol.
		
		case Packet.CLIENT_LOCATION : //Location
			clientPacket = new LocationPacket(Arrays.copyOf(packet, 1));
			double[] location = new double[2];
			location = ((LocationPacket) clientPacket).getLocation();
			System.out.println(location[0] + " | " + location[1]);
			//sendPacket();
			break;
			
		case Packet.PING_RESPONSE : //Ping Response
			//Server needs to send back ping response
			break;
			
		case Packet.CATCH_PERFORMED : //Catch Performed	
			//Server needs to complete some kind of action to check if player has been caught
			//If Player has been caught successfully then response needs to be given
			break;
			
		case Packet.CAPTURED : //Captured by pursuer
			//Server will need to check if action is correct. Leaderboard and player instances will be updated
			//Server will need to respond appropiately.
			break;
			
		case Packet.ABILITY_USAGE : //Ability Usage
			int[] ability = new int[1]; //Ability Data Type might need changing?
			//Some sort of ability manipulation needed here
			//Server may need response if ability changes an element of the game
			break;
			
		case Packet.VOTE : //Vote in a Lobby
			int[] gameMode = new int[1]; //Gamemode Data Type change again?
			//Server sends a response updating vote count
			break;
			
		case Packet.REPORT : //Report?
			//??
			break;
			
		case Packet.QUIT : //Client quits
			//Server removes player instance from the game
			//Server sends broadcast to all clients notifying that a player has left
			break;
		
		case Packet.JOIN : //Client joins
			int[] roomKey = new int[6]; //Room Key
			double[] macAddress = new double[10]; //Mac Address
			//Server checks room key and completes some kind of action
			break;
			
		case Packet.HOST_ACTION : //Action completed by a host
			int[] hostID = new int[2]; //Host Action ID
			//Seperate Case statement will be needed for this
			//Server will complete an action depending on the ID
			break;
			
		case Packet.CLIENT_NAK : //NAK Resend packet
			//Server will resend packet to client
			break;
			
		case Packet.CLIENT_ACK : //ACK Client Acknowledgement of last packet
			//Confirmation that packet was sent
			//No response required by server
			break;
			
		case Packet.BAD_SPAWN : //Bad Spawn - MAY NOT BE NEEDED ANYMORE IF WE ARENT IMPLEMENTING SPAWN
			//Server will provide a new spawn
			break;
			
		case Packet.PLAYER_READY : //Player has reached their spawn - AGAIN MAY NOT BE NEEDED
			//Game starts..
			break;
			
			
		default : //?? not sure what should happen in the event the data ID isn't recognised
			
			break;
		}
	}
	
	private void sendPacket(){
		System.out.println("Returning Packet");
		Server.sendPacket(clientID, serverPacket.getPacket());
	}
	
}