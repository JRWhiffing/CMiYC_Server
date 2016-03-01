package packets.serverPackets.broadcastPackets;

import packets.Packet;

public class VotesPacket extends Packet {

	public VotesPacket(){
		putByte(Packet.BROADCAST);
		putByte(Packet.BROADCAST_VOTES);
	}
	
	public VotesPacket(byte[] data){
		packet = data;
	}
	
	public void putVotes(int[] votes){
		putInt(votes.length);
		for(int i = 0; i < votes.length; i++){
			putInt(votes[i]);
		}
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