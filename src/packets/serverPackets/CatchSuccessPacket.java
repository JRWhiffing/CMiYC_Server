package packets.serverPackets;

import packets.Packet;

public class CatchSuccessPacket extends Packet {

	public CatchSuccessPacket() {
		putByte(Packet.CATCH_SUCCESS);
	}
	
	public CatchSuccessPacket(byte[] data){
		packet = data;
	}
	
	public void putSuccess(byte success){
		putByte(success);
	}
	
	public byte getSuccess(){
		return getByte();
	}
}
