package packetParsers;

import java.util.Arrays;

import packets.*;
import packets.clientPackets.*;
import server.Server;

/**
 * Class for parsing client packets
 * Uses a Case statement to distinguish between packets
 * @authors James and Adam
 *
 */
public class PacketParser {
	
	private final int clientID; //Used to determine which player is making the request
	private String roomKey; //Used to determine which room to perform changes to
	private final HostActionPacketParser hostActionParser;
	
	/**
	 * Constructor that sets the clientID for the parser
	 * @param clientID - The integer ID of the client
	 */
	public PacketParser(int clientID) {
		this.clientID = clientID;
		hostActionParser = new HostActionPacketParser(clientID); //Creates the Host Action Packet Parser for if/when the player is host
	}
	
	/**
	 * Method for processing client packets
	 * @param packet - Byte data of the packet
	 */
	public void processPacket(byte[] packet) {
		byte dataID = packet[0]; //First byte is the host packet ID
		System.out.println("Data ID: "+dataID);
		byte[] data = Arrays.copyOfRange(packet, 1, packet.length); //Removes the ID from the packet so only data is left
		
		//Each Case is a Protocol
		switch (dataID) {
		
		//Location of the Client
		case Packet.LOCATION :
			LocationPacket locationPacket = new LocationPacket(data);
			Server.setLocation(roomKey, clientID, locationPacket.getLocation());
			break;
		
		//Ping Response from Client
		case Packet.PING_RESPONSE :
			Server.pingResponse(roomKey, clientID);
			break;
		
		//The client has caught its target
		case Packet.CATCH_PERFORMED :
			Server.catchPerformed(roomKey, clientID); //ClientID being the players ID not the caught player
			//Server needs to complete some kind of action to check if player has been caught
			//If Player has been caught successfully then response needs to be given
			break;
		
		//The client has been captured	
		case Packet.CAPTURED :
			Server.captured(roomKey, clientID);
			//Server will need to respond appropiately.
			break;
		
		//The client has used an ability	
		case Packet.ABILITY_USAGE :
			AbilityUsagePacket abilityPacket = new AbilityUsagePacket(data);
			Server.abilityUsage(roomKey, clientID, abilityPacket.getAbility());
			//Server may need response if ability changes an element of the game
			break;
			
		//A vote for picking the game type	
		case Packet.VOTE :
			VotePacket votePacket = new VotePacket(data);
			Server.vote(roomKey, clientID, votePacket.getVote());
			//Server sends a response updating vote count
			break;
			
		//A player has been reported	
		case Packet.REPORT :
			ReportPacket reportPacket = new ReportPacket(data);
			Server.playerReported(roomKey, reportPacket.getReport(), clientID);
			break;
			
		//The client wishes to quit the game	
		case Packet.QUIT : //Client quits
			Server.quitPlayer(roomKey, clientID);
			//Server sends broadcast to all clients notifying that a player has left
			break;
		
		//The client wishes to join the game	
		case Packet.JOIN :
			JoinPacket joinPacket = new JoinPacket(data);
			Server.playerJoin(joinPacket.getRoomKey(), joinPacket.getMACAddress(), joinPacket.getPlayerName(), clientID);
			break;
		
		//The client performs a host action	- Needs to make sure he is host
		case Packet.HOST_ACTION :
			//The packet is assessed by the Host Action Parser
			System.out.println("Handling host packet");
			hostActionParser.processHostAction(dataID, data);
			break;
		
		//Client sends an acknowledge of the last packet	
		case Packet.ACK :
			Server.acknowledgement(roomKey, clientID);
			break;
		
		//Not needed anymore	
		case Packet.BAD_SPAWN :
			//Server.badSpawn(roomKey, clientID);
			//Server will provide a new spawn
			break;
			
		//Not needed anymore	
		case Packet.PLAYER_READY :
			//Server.playerReady(roomKey, clientID);
			break;
			
		default : 
			String bytes = dataID + " | ";
			for (int i = 0; i < packet.length; i ++) {
				bytes += data[i] + " | ";
			}
			System.err.println("Unrecognised packet: \"" + bytes +
					"\"\n From client: " + clientID + ", in room: " + roomKey);
			Server.quitPlayer(roomKey, clientID);
			break;
		}
	}
	
	/**
	 * Method for setting the room key for both the client packet parser
	 * and the host action packet parser
	 * @param key - The Room Key
	 */
	public void setRoomKey(String key) {
		roomKey = key;
		hostActionParser.setRoomKey(key);
	}
	
}