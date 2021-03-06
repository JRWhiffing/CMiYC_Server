package server;

import java.net.Socket;

import packets.Packet;

public class ClientHandler {
	private final int clientID;
	private final ServerInput sInput;
	private final ServerOutput sOutput;
	
	public ClientHandler(Socket cs, int clientID){
		this.clientID = clientID;
		sInput = new ServerInput(cs, clientID);
		sOutput = new ServerOutput(cs);
		sInput.start();
		sOutput.start();
	}
	
	public synchronized void sendPacket(Packet serverPacket) {
		System.out.println("Sending Packet: "+ serverPacket.size());
		sOutput.addPacketToQueue(serverPacket);
	}
	
	public int getClientID(){
		return (int) clientID;
	}
	
	public void setRoomKey(String key){
		sInput.setRoomKey(key);
	}
	
	public void close(){
		if(!sInput.isInterrupted()){
			sInput.close();
		}
		if(!sOutput.isInterrupted()){
			sOutput.close();
		}
		while(!sInput.isInterrupted()||!sOutput.isInterrupted()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.err.println("Sleep failed: " + e.getMessage());;
			}
		}
		System.out.println("Client " + clientID + " has gone.");
	}
	
}