package server;

import server.packets.*;

public class StateMachine {
	
	private final int clientID;
	private ClientPacket c_Packet;
	protected ServerPacket s_Packet;
	
	StateMachine(Integer clientID){
		//can be used in the event of knowing which thread to close when disconnecting.
		this.clientID = clientID;
	}
	
	//Must have method for each protocol, either hear or from across System.
	
	public void stateMachine(byte[] packet){//not sure on best way to pass data to StateMachine
		s_Packet = new ServerPacket();
		c_Packet = new ClientPacket(packet);
		byte dataID;
		if(c_Packet.getByte() == 0x01){//check that the checksum passed.
			c_Packet.getByte();//getting rid of the senderID
			dataID = c_Packet.getByte();
		} else {
			dataID = 0x00; //if the checksum failed set the id to 0x00 as this will be handled by the default case.
		}
		
		switch (dataID){//each case is a protocol.
		
		case 0x01 :
			double[] location = new double[2];
			location[0] = c_Packet.getDouble();
			location[1] = c_Packet.getDouble();
			//testing response
			s_Packet.putByte(ServerPacket.LOCATION);
			s_Packet.putDouble(location[0]);
			s_Packet.putDouble(location[1]);
			sendPacket();
			break;
			
		default : //The NAK response to requesting that the last packet be resent.
			System.err.println("Unrecognized Data ID");
			s_Packet.putByte(ServerPacket.NAK);
			s_Packet.putByte(ServerPacket.NAK_RESEND);
			sendPacket();
			break;
		}
	}
	
	private void sendPacket(){
		System.out.println("Returning Packet");
		Server.sendPacket(clientID, s_Packet.getPacket());
	}
	
}