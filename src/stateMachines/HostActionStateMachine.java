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
			break;
		
			
		case Packet.HOST_ACTION_CLOSE_ROOM :
			
			break;
			
		case Packet.HOST_ACTION_CREATE_ROOM :
			
			break;
			
		case Packet.HOST_ACTION_END_ROUND :
			
			break;
			
		case Packet.HOST_ACTION_KICK_PLAYER :
			
			break;
			
		case Packet.HOST_ACTION_SCORE_LIMIT :
			
			break;
			
		case Packet.HOST_ACTION_SET_BOUNDARIES :
			
			break;
			
		case Packet.HOST_ACTION_START_ROUND :
			
			break;
			
		case Packet.HOST_ACTION_TIME_LIMIT :
			
			break;
			
		default : //?? not sure what should happen in the event the data ID isn't recognised
			
			break;
		}
	}
	
}
