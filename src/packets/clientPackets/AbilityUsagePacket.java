package packets.clientPackets;

import packets.Packet;

public class AbilityUsagePacket extends Packet{

	public AbilityUsagePacket(){
		putByte(ABILITY_USAGE);
	}
	
	public AbilityUsagePacket(byte[] data){
		packet = data;
	}
	
	public void putAbility(byte ability){
		putByte(ability);
	}
	
	//method to put each ability?
	
//	public void decoy(){//method to put each ability?
//		putByte(Packet.ABILITY_DECOY);
//	}
//	
//	public void hide(){//method to put each ability?
//		putByte(Packet.ABILITY_HIDE);
//	}
//	
//	public void ping(){//method to put each ability?
//		putByte(Packet.ABILITY_PING);
//	}
//	
//	public void sneak(){//method to put each ability?
//		putByte(Packet.ABILITY_SNEAK);
//	}
	
	public byte getAbility(){
		return getByte();
	}
	
}