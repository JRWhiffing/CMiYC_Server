package stateMachines;

import java.util.Arrays;

import packets.*;
import packets.clientPackets.*;
import server.Server;

public class StateMachine {
	
	private final int clientID;//to determine which player is making the request.
	private final HostActionStateMachine hasm;
	private String roomKey;//for determining which room to perform changes to.
	
	public StateMachine(int clientID){
		//can be used in the event of knowing which thread to close when disconnecting.
		this.clientID = clientID;
		hasm = new HostActionStateMachine(clientID);
	}
	
	public void processPacket(byte[] packet){//not sure on best way to pass data to StateMachine
		byte dataID = packet[0];
		packet = Arrays.copyOf(packet, 1);//removing the data ID from the packet so only data is left
		
		switch (dataID){//each case is a protocol.
		
		case Packet.LOCATION : //Location
			LocationPacket lp = new LocationPacket(packet);
			//Server.setLocation(roomKey, clientID, lp.getLocation());
			//sendPacket();
			break;
			
		case Packet.PING_RESPONSE : //Ping Response
			//Server.pingResponse(roomKey, clientID);
			//Server needs to send back ping response - nope, this is the response from the client.
			break;
			
		case Packet.CATCH_PERFORMED : //Catch Performed	
			//Server.catchPerformed(roomKey, clientID);
			//Server needs to complete some kind of action to check if player has been caught
			//If Player has been caught successfully then response needs to be given
			break;
			
		case Packet.CAPTURED : //Captured by pursuer
			//Server.captured(roomKey, clientID);
			//Server will need to check if action is correct. Leaderboard and player instances will be updated
			//Server will need to respond appropiately.
			break;
			
		case Packet.ABILITY_USAGE : //Ability Usage
			AbilityUsagePacket aup = new AbilityUsagePacket(packet);
			//Server.abilityUsage(roomKey, clientID, aup.getAbility());
			//Some sort of ability manipulation needed here
			//Server may need response if ability changes an element of the game
			break;
			
		case Packet.VOTE : //Vote in a Lobby
			VotePacket vp = new VotePacket(packet);
			//Server.vote(roomKey, clientID, vp.getVote());
			//Server sends a response updating vote count
			break;
			
		case Packet.REPORT : //Report?
			ReportPacket rp = new ReportPacket(packet);
			//Server.playerReported(roomKey, rp.getReport(), clientID);
			//Player reported and the player who reported them.
			//??
			break;
			
		case Packet.QUIT : //Client quits
			//Server.playerQuit(roomKey, clientID);
			//Server removes player instance from the game
			//Server sends broadcast to all clients notifying that a player has left
			break;
		
		case Packet.JOIN : //Client joins
			JoinPacket jp = new JoinPacket(packet);
			//Server.playerJoin(jp.getRoomKey(), jp.getMACAddress(), jp.getPlayerName());
			//Server checks room key and completes some kind of action
			break;
			
		case Packet.HOST_ACTION : //Action completed by a host
			hasm.processHostAction(dataID, packet);
			//Separate Case statement will be needed for this
			//Server will complete an action depending on the ID
			break;
			
		case Packet.ACK : //ACK Client Acknowledgement of last packet
			//Server.ack(roomKey, clientID);
			//Confirmation that packet was sent
			//No response required by server
			break;
			
		case Packet.BAD_SPAWN : //Bad Spawn - MAY NOT BE NEEDED ANYMORE IF WE ARENT IMPLEMENTING SPAWN
			//Server.badSpawn(roomKey, clientID);
			//Server will provide a new spawn
			break;
			
		case Packet.PLAYER_READY : //Player has reached their spawn - AGAIN MAY NOT BE NEEDED
			//Server.playerReady(roomKey, clientID);
			//Game starts..
			break;
			
		default : 
			String bytes = dataID + " | ";
			for(int i = 0; i < packet.length; i ++){
				bytes += packet[i] + " | ";
			}
			System.err.println("Unrecognised packet: \"" + bytes +
					"\"\n From client: " + clientID + ", in room: " + roomKey);
			Server.closeClient(roomKey, clientID);
			break;
		}
	}
	
	public void setRoomKey(String key){
		roomKey = key;
		hasm.setRoomKey(key);
	}
	
}