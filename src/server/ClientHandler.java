package server;

import java.net.Socket;

import server.packets.Packet;

public class ClientHandler {
	private final int clientID;
	private final ServerInput sInput;
	private final ServerOutput sOutput;
	
	public ClientHandler(Socket cs, int clientID){
		this.clientID = clientID;
		sInput = new ServerInput(cs, clientID);
		sOutput = new ServerOutput(cs);
		sInput.run();
		sOutput.run();
	}
	
	public synchronized void sendPacket(Packet serverPacket) {
		System.out.println("Sending Packet");
		sOutput.addPacketToQueue(serverPacket);
	}
	
	public int getClientID(){
		return (int) clientID;
	}
	
	public void close(){
		sInput.close();
		sOutput.close();
		System.out.println("ClientHandler" + clientID + ": Client " + clientID + " has gone.");
	}
	
}