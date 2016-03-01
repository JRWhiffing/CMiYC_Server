package packets.serverPackets.lobbyInfoPackets;

import packets.Packet;

public class VotesPacket extends Packet {

	public VotesPacket(){
		putByte(Packet.LOBBYINFO);
		putByte(Packet.LOBBYINFO_VOTES);
	}
	
	public VotesPacket(byte[] data){
		packet = data;
	}
	
	public void VotesEnabled(){
		putByte(Packet.VOTES_ENABLED);
	}
	
	public void VotesDisabled(){
		putByte(Packet.VOTES_DISABLED);
	}
	
	public void putVotes(int[] votes){
		putInt(votes.length);
		for(int i = 0; i < votes.length; i++){
			putInt(votes[i]);
		}
	}
	
	public byte getVotesEnabled(){
		return getByte();
	}
	
	public int[] getVotes(){
		int length = getInt();
		int[] votes = new int[length];
		for(int i = 0; i < length; i++){
			votes[i] = getInt();
		}
		return votes;
	}
	
}