package stateMachines;

import java.util.Arrays;

import packets.*;
//needed?
//import server.packets.serverPackets.*;
import packets.clientPackets.*;
import server.Server;

public class StateMachine {
	
	private final int clientID;
	private final HostActionStateMachine hasm;
	
	public StateMachine(int clientID){
		//can be used in the event of knowing which thread to close when disconnecting.
		this.clientID = clientID;
		hasm = new HostActionStateMachine(clientID);
	}
	
	//Must have method for each protocol, either hear or from across System.
	
	public void processPacket(byte[] packet){//not sure on best way to pass data to StateMachine
		byte dataID = packet[0];
		packet = Arrays.copyOf(packet, 1);//removing the data ID from the packet so only data is left
		
		switch (dataID){//each case is a protocol.
		
		case Packet.LOCATION : //Location
			LocationPacket lp = new LocationPacket(packet);
			double[] location = new double[2];
			location = lp.getLocation();
			System.out.println(location[0] + " | " + location[1]);
			//sendPacket();
			break;
			
		case Packet.PING_RESPONSE : //Ping Response
			//Server needs to send back ping response - nope, this is the response from the client.
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
			AbilityUsagePacket aup = new AbilityUsagePacket(packet);
			byte ability = aup.getAbility(); //Ability Data Type might need changing?
			System.out.println(ability);
			//Some sort of ability manipulation needed here
			//Server may need response if ability changes an element of the game
			break;
			
		case Packet.VOTE : //Vote in a Lobby
			VotePacket vp = new VotePacket(packet);
			byte gametype = vp.getVote();
			System.out.println(gametype);
			//Server sends a response updating vote count
			break;
			
		case Packet.REPORT : //Report?
			ReportPacket rp = new ReportPacket(packet);
			int reportedPlayerID = rp.getReport();
			System.out.println(reportedPlayerID);
			//??
			break;
			
		case Packet.QUIT : //Client quits
			//Server removes player instance from the game
			//Server sends broadcast to all clients notifying that a player has left
			break;
		
		case Packet.JOIN : //Client joins
			JoinPacket jp = new JoinPacket(packet);
			String roomKey = jp.getRoomKey(); //Room Key
			double[] macAddress = jp.getMACAddress(); //Mac Address
			
			System.out.println(roomKey + " | " + macAddress[0] + "-" + macAddress[1] + "-"
					 + macAddress[2] + "-" + macAddress[3] + "-" + macAddress[4] + "-" + macAddress[5]);
			//Server checks room key and completes some kind of action
			break;
			
		case Packet.HOST_ACTION : //Action completed by a host
			hasm.processHostAction(packet);
			//Separate Case statement will be needed for this
			//Server will complete an action depending on the ID
			break;
			
		case Packet.ACK : //ACK Client Acknowledgement of last packet
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
	
	//will the state machine actually send packets, or will the methods called within the server send the packets?
	private void sendPacket(Packet serverPacket){
		System.out.println("Returning Packet");
		Server.sendPacket(clientID, serverPacket);
	}
	
}