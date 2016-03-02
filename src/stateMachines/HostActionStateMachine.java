package stateMachines;

import java.util.Arrays;

import packets.Packet;
import packets.clientPackets.hostPackets.*;
import server.Server;

public class HostActionStateMachine {
	
	private final int clientID;
	private String roomKey;//for determining which room to perform changes to.
	
	public HostActionStateMachine(int clientID){
		this.clientID = clientID;
	}

	public void processHostAction(byte[] data){
		byte hostID = data[0]; //Host Action ID
		data = Arrays.copyOf(data, 1);//removing the host ID from the packet so only data is left
		
		switch (hostID){
		
		case Packet.HOST_ACTION_ALLOW_VOTING :
			//Server.toggleVoting(roomKey);
			break;
		
		case Packet.HOST_ACTION_BOUNDARY_UPDATES:
			BoundaryUpdatesPacket bup = new BoundaryUpdatesPacket(data);
			//Server.boundaryUpdates(roomKey, bup.getBoundaryUpdates());
			break;
			
		case Packet.HOST_ACTION_CHANGE_GAMETYPE :
			GametypePacket gp = new GametypePacket(data);
			//Server.changeGametype(roomKey, gp.getGameType());
			break;
		
		case Packet.HOST_ACTION_CHANGE_HOST :
			ChangeHostPacket chp = new ChangeHostPacket(data);
			//Server.changeHost(roomKey, chp.getHostID());
			break;
		
		case Packet.HOST_ACTION_CLOSE_ROOM :
			//Server.closeRoom(roomKey);
			//Current room is closed - No Additional Data Needed
			//Server informs all players that room is closing
			break;
			
		case Packet.HOST_ACTION_CREATE_ROOM ://don't need room key as won't have one
			CreateRoomPacket crp = new CreateRoomPacket(data);
			//Server.createRoom(clientID, crp.getRoomName(), crp.getHostName(), crp.getMACAddress());
			//Creating a new room - Room Name + Mac Address Needed from Packet
			//Creates new room instance + Room Key
			//Player sending request is now host of game
			break;
			
		case Packet.HOST_ACTION_END_ROUND :
			//Server.endRound(roomKey);
			//Round is ended - No Additional Data Needed
			//Server needs to inform all Clients within the room that the round is over
			break;
			
		case Packet.HOST_ACTION_KICK_PLAYER :
			KickPlayerPacket kpp = new KickPlayerPacket(data);
			//Server.kickPlayer(roomKey, kpp.getPlayerID());
			//Kicking another Player - Player ID is Needed from Packet
			//Server informs player that they have been kicked and then broadcasts to each client with new leaderboard
			break;
			
		case Packet.HOST_ACTION_SCORE_LIMIT :
			ScoreLimitPacket slp = new ScoreLimitPacket(data);
			//Server.setScoreLimit(roomKey slp.getScoreLimit());
			//Alter score limit - Score Limit is Needed from Packet
			//Server informs all players of updated score for next game
			break;
			
		case Packet.HOST_ACTION_SET_BOUNDARIES :
			SetBoundariesPacket sbp = new SetBoundariesPacket(data);
			//Server.setBoundaries(roomKey, sbp.getBoundariesCentre(), sbp.getBoundaryRadius);
			//Alter boundary - Longitude + Latitude + Radius are Needed from Packet
			//Server informs all players of updated boundary for next game
			break;
			
		case Packet.HOST_ACTION_START_ROUND :
			//Server.startRound(roomKey);
			//Round is started - No Additional Data Needed
			//Needs to check if theres enough players BEFORE starting the game
			break;
			
		case Packet.HOST_ACTION_TIME_LIMIT :
			TimeLimitPacket tlp = new TimeLimitPacket(data);
			//Server.setTimeLimit(roomKey, tlp.getTimeLimit());
			//Alter the time limit - Time Limit (In Minutes) is Needed from Packet
			//Server informs all players of updated time for next game
			break;
			
		default : //?? not sure what should happen in the event the data ID isn't recognised - Surely this would not happen though? 
			
			break;
		}
	}

	public void setRoomKey(String key){
		roomKey = key;
	}
	
}