package stateMachines;

import java.util.Arrays;

import packets.Packet;
import packets.clientPackets.hostPackets.*;
import server.Server;

public class HostActionStateMachine {
	
	private final int clientID;
	
	public HostActionStateMachine(int clientID){
		this.clientID = clientID;
	}

	public void processHostAction(byte[] data){
		byte hostID = data[0]; //Host Action ID
		data = Arrays.copyOf(data, 1);//removing the host ID from the packet so only data is left
		
		switch (hostID){
		
		case Packet.HOST_ACTION_ALLOW_VOTING :
<<<<<<< HEAD
			//Server.toggleVoting(clientID);
			break;
		
		case Packet.HOST_ACTION_BOUNDARY_UPDATES:
			BoundaryUpdatesPacket bup = new BoundaryUpdatesPacket(data);
			//Server.boundaryUpdates(bup.getBoundaryUpdates());
			break;
			
		case Packet.HOST_ACTION_CHANGE_GAMETYPE :
			GametypePacket gp = new GametypePacket(data);
			//Server.changeGametype(gp.getGameType());
			break;
		
		case Packet.HOST_ACTION_CHANGE_HOST :
			ChangeHostPacket chp = new ChangeHostPacket(data);
			//Server.changeHost(chp.getHostID());
=======
			AllowVotingPacket avp = new AllowVotingPacket();
			//Tells server to toggle voting - No Additional Data Needed
			break;
		
		case Packet.HOST_ACTION_BOUNDARY_UPDATES:
			//Boundary is updated - Time Interval + Percentage are Needed from Packet
			//Server updates boundaries. 
			//Don't the players need to know about this change??
			break;
			
		case Packet.HOST_ACTION_CHANGE_GAMETYPE :
			//Gamemode is changed - Gamemode is Needed from Packet
			//Server sends updated gamemode to each client in lobby for next game
			break;
		
		case Packet.HOST_ACTION_CHANGE_HOST :
			//Host changes - Player ID Needed from Packet
			//Server sends updated Host to each client in lobby + New Host gets special packet informing them that they are the new host
>>>>>>> 152b735fdcb689d20951d1e38c64e2002715150d
			break;
		
			
		case Packet.HOST_ACTION_CLOSE_ROOM :
			//Current room is closed - No Additional Data Needed
			//Server informs all players that room is closing
			break;
			
		case Packet.HOST_ACTION_CREATE_ROOM :
			//Creating a new room - Room Name + Mac Address Needed from Packet
			//Creates new room instance + Room Key
			//Player sending request is now host of game
			break;
			
		case Packet.HOST_ACTION_END_ROUND :
			//Round is ended - No Additional Data Needed
			//Server needs to inform all Clients within the room that the round is over
			break;
			
		case Packet.HOST_ACTION_KICK_PLAYER :
			//Kicking another Player - Player ID is Needed from Packet
			//Server informs player that they have been kicked and then broadcasts to each client with new leaderboard
			break;
			
		case Packet.HOST_ACTION_SCORE_LIMIT :
			//Alter score limit - Score Limit is Needed from Packet
			//Server informs all players of updated score for next game
			break;
			
		case Packet.HOST_ACTION_SET_BOUNDARIES :
			//Alter boundary - Longitude + Latitude + Radius are Needed from Packet
			//Server informs all players of updated boundary for next game
			break;
			
		case Packet.HOST_ACTION_START_ROUND :
			//Round is started - No Additional Data Needed
			//Needs to check if theres enough players BEFORE starting the game
			break;
			
		case Packet.HOST_ACTION_TIME_LIMIT :
			//Alter the time limit - Time Limit (In Minutes) is Needed from Packet
			//Server informs all players of updated time for next game
			break;
			
		default : //?? not sure what should happen in the event the data ID isn't recognised - Surely this would not happen though? 
			
			break;
		}
	}
	
}
