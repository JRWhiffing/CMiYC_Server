package server.stateMachines;

import java.util.Arrays;

import server.packets.Packet;
import server.packets.clientPackets.hostPackets.*;
import server.Server;

public class HostActionStateMachine {

	private Packet clientPacket;
	private Packet serverPacket;
	
	public void processHostAction(byte[] hostAction){
		byte hostID = hostAction[0]; //Host Action ID
		hostAction = Arrays.copyOf(hostAction, 1);//removing the host ID from the packet so only data is left
		
		switch (hostID){
		
		case Packet.HOST_ACTION_ALLOW_VOTING :
			//clientPacket = new AllowVotingPacket
			break;
		
		case Packet.HOST_ACTION_BOUNDARY_UPDATES:
			
			break;
			
		case Packet.HOST_ACTION_CHANGE_GAMEMODE :
			
			break;
		
		case Packet.HOST_ACTION_CHANGE_HOST :
			
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
