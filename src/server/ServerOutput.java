package server;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import packets.Packet;

public class ServerOutput extends Thread{
	private final Socket clientSocket;
	private LinkedList<Packet> packetQueue;
	private boolean active;

	public ServerOutput(Socket cs){
		clientSocket = cs;
		active = true;
	}
	
	@Override
	public void run(){
		while(active){
			if(packetQueue.size() > 0){
				for(Packet serverPacket : packetQueue){
					sendPacket(serverPacket.getPacket());
				}
			}
		}
	}
	
	private void sendPacket(byte[] server_Packet) {
		System.out.println("Sending Packet");
		try {
			clientSocket.getOutputStream().write(server_Packet);
			clientSocket.getOutputStream().flush();
			System.out.println("Packet Sent");
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			ioe.printStackTrace(System.err);
			close();
		}
	}
	
	public void addPacketToQueue(Packet serverPacket){
		packetQueue.add(serverPacket);
	}
	
	public void close(){
		if (clientSocket != null){
			try {
				clientSocket.close();
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
			}
		}
		if (!Thread.currentThread().isInterrupted()){
			Thread.currentThread().interrupt();
		}
	}
	
}