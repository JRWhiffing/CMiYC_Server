package room;

import packets.serverPackets.CatchSuccessPacket;
import server.Server;

/**
 * A thread to be instantiated by the packet parser to run a timer
 * and then check whether the capture has been acknowledged by the
 * target, if so the thread just interrupts itself, otherwise it will
 * send a message to both parties, stating that the capture has been
 * voided.
 */

public class CaptureTimer extends Thread {

	private final String roomKey;
	private final int pursuerID;
	private final int targetID;
	
	public CaptureTimer(String key, int pID, int tID){
		roomKey = key;
		pursuerID = pID;
		targetID = tID;
	}
	
	public void run(){
		//How long should the server wait for the capture to be 
		//acknowledged by the target? currently 4 seconds.
		try {
			Thread.currentThread().sleep(6500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!Server.getRoom(roomKey).getPlayer(pursuerID).getState().equals("CONNECTED")){
			CatchSuccessPacket csp = new CatchSuccessPacket();
			csp.putSuccess((byte) 0x00);
			Server.sendPacket(pursuerID, csp);
		}
	}
	
}