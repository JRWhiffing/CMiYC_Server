package packetParsers;

import java.util.Arrays;

import packets.Packet;
import packets.clientPackets.hostPackets.*;
import server.Server;

/**
 * Class for parsing host action packets
 * Uses a Case statement to distinguish between packets
 * @authors James and Adam
 *
 */
public class HostActionPacketParser {
	
	private final int clientID; //Used to determine which player is making the request
	private String roomKey; //Used to determine which room to perform changes to
	
	/**
	 * Constructor that sets the clientID for the parser
	 * @param clientID - The integer ID of the client
	 */
	public HostActionPacketParser(int clientID) {
		this.clientID = clientID;
	}
	
	/**
	 * Method for processing the host actions
	 * @param dataID - The ID of the Data (Should be Host Action)
	 * @param data - The data of the packet
	 */
	public void processHostAction(int dataID, byte[] data) {
		byte hostPacketID = data[0]; //First byte is the host packet ID
		data = Arrays.copyOf(data, 1); //Removes the ID from the packet so only data is left
		
		switch (hostPacketID) {
		
		//Allows voting to take place
		case Packet.HOST_ACTION_ALLOW_VOTING :
			Server.toggleVoting(roomKey);
			break;
		
		//Sets the boundary update interval and the percentage of boundary decrease
		case Packet.HOST_ACTION_BOUNDARY_UPDATES:
			BoundaryUpdatesPacket bup = new BoundaryUpdatesPacket(data);
			Server.setBoundariesUpdates(roomKey, bup.getBoundaryUpdates());
			break;
		
		//Changes the Game Type
		case Packet.HOST_ACTION_CHANGE_GAMETYPE :
			GametypePacket gp = new GametypePacket(data);
			Server.changeGameType(roomKey, gp.getGameType());
			break;
		
		//Changes the host to a new host that the current host has selected
		case Packet.HOST_ACTION_CHANGE_HOST :
			ChangeHostPacket chp = new ChangeHostPacket(data);
			Server.changeHost(roomKey, chp.getHostID());
			break;
		
		//Closes the room
		case Packet.HOST_ACTION_CLOSE_ROOM :
			Server.closeRoom(roomKey);
			//Server informs all players that room is closing
			break;
		
		//Creates the room for the first time
		//Room Key not needed as it will create one now	
		case Packet.HOST_ACTION_CREATE_ROOM :
			CreateRoomPacket crp = new CreateRoomPacket(data);
			Server.createRoom(clientID, crp.getRoomName(), crp.getHostName(), crp.getMACAddress());
			break;
		
		//Ends the current round	
		case Packet.HOST_ACTION_END_ROUND :
			Server.endGame(roomKey);
			//Server needs to inform all Clients within the room that the round is over
			break;
		
		//Kicks the clientID from the game
		case Packet.HOST_ACTION_KICK_PLAYER :
			KickPlayerPacket kpp = new KickPlayerPacket(data);
			Server.kickPlayer(roomKey, kpp.getPlayerID());
			//NEED TO MAKE SURE THE HOST CANNOT KICK ITSELF
			//Server informs player that they have been kicked and then broadcasts to each client with new leaderboard
			break;
		
		//Sets the score limit of the game
		case Packet.HOST_ACTION_SCORE_LIMIT :
			ScoreLimitPacket slp = new ScoreLimitPacket(data);
			Server.setScoreLimit(roomKey, slp.getScoreLimit());
			//Server informs all players of updated score for next game
			break;
		
		//Sets the boundaries of the game using a location centre point (Longitude and Latitude) and the radius	
		case Packet.HOST_ACTION_SET_BOUNDARIES :
			SetBoundariesPacket sbp = new SetBoundariesPacket(data);
			Server.setBoundaries(roomKey, sbp.getBoundariesCentre(), sbp.getBoundaryRadius());
			//Server informs all players of updated boundary for next game
			break;
		
		//Starts the round	
		case Packet.HOST_ACTION_START_ROUND :
			Server.startGame(roomKey);
			//Needs to check if there is enough players BEFORE starting the game
			break;
		
		//Sets the time limit of the game (In minutes)
		case Packet.HOST_ACTION_TIME_LIMIT :
			TimeLimitPacket tlp = new TimeLimitPacket(data);
			Server.setTimeLimit(roomKey, tlp.getTimeLimit());
			//Server informs all players of updated time for next game
			break;
		
		//Produces an error message if the data ID is not recognised	
		default :
			String bytes = dataID + " | " + hostPacketID + " | ";
			for (int i = 0; i < data.length; i++) {
				bytes += data[i] + " | ";
			}
			System.err.println("Unrecognised packet: \"" + bytes +
					"\"\n From client: " + clientID + ", in room: " + roomKey);
			Server.quitPlayer(roomKey, clientID);
			break;
		}
	}
	
	/**
	 * Method for setting the room key
	 * @param key - The Room Key 
	 */
	public void setRoomKey(String key) {
		roomKey = key;
	}
	
}