package server;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import packets.Packet;

public class ServerOutput extends Thread{
	private final Socket clientSocket;
	private LinkedList<Packet> packetQueue = new LinkedList<Packet>();
	private boolean active;

	public ServerOutput(Socket cs){
		clientSocket = cs;
		active = true;
	}
	
	@Override
	public void run(){
		while(active){
			try {
				if ((packetQueue.size()) > 0) {
					clientSocket.getOutputStream().write(packetQueue.pop().getPacket());
					clientSocket.getOutputStream().flush();
					Thread.sleep(10);
					System.out.println("Packet Sent");
				}
				Thread.sleep(10);
			} catch (IOException | InterruptedException e) {
				break;
			}	
		}
	}
	
	public void addPacketToQueue(Packet serverPacket){
		packetQueue.add(serverPacket);
	}
	
	public void close(){
		if (clientSocket != null){
			if(!clientSocket.isClosed()){
				try {
					clientSocket.close();
				} catch (IOException ioe) {
					System.err.println(ioe.getMessage());
				}
				System.out.println("output closing");
			}
		}
		if (!Thread.currentThread().isInterrupted()){
			Thread.currentThread().interrupt();
		}
	}
	
}