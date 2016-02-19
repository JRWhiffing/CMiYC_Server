package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import server.Server;
import server.packets.ServerPacket;

public class ClientHandler extends Thread{
	private final int clientID;
	private final Socket clientSocket;
	private final StateMachine sm;
	
	public ClientHandler(Socket cs, int clientID){
		this.clientID = clientID;
		clientSocket = cs;
		sm = new StateMachine(clientID);
	}
	
	@Override
	public void run() {
		int read = -1;
		byte[] clientPacket = new byte[0];
		byte[] temp = new byte[512];
		try{
			while((read = clientSocket.getInputStream().read(temp, 0, temp.length)) > -1){
				System.out.println("Message Recieved, Processing.");
				clientPacket = Arrays.copyOfRange(temp, 0, read);
				sm.stateMachine(checkPacket(clientPacket));
				clientPacket = new byte[0];
			}
		} catch (Exception e){
			System.err.println(e.getMessage());
			Server.closeServer();
		}
	}
	
	private byte[] checkPacket(byte[] packet){
		byte[] newPacket = new byte[packet.length];
		byte checksum = packet[packet.length - 1];
		byte checksum_ = 0x00;
		
		for(int i = 0; i < packet.length - 1; i++){
			checksum_ += packet[i];
			newPacket[i + 1] = packet[i];//could replace with an Array copy thingy.
		}
		//if first Byte is 0 then packet checksum failed, need to ask for re-send.
		//Rest of array is the packet minus the checksum. Can place in separate array afterwards.
		if(checksum == checksum_){
			newPacket[0] = 1;
			System.out.println("Checksum Successful");
		} else {
			newPacket[0] = 0;
			System.out.println("Checksum Unsuccessful");
		}
		
		return newPacket;
	}
	
	public void sendPacket(byte[] server_Packet) {
		System.out.println("Sending Packet");
		try {
			clientSocket.getOutputStream().write(server_Packet);
			clientSocket.getOutputStream().flush();
			System.out.println("Packet Sent");
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			ioe.printStackTrace(System.err);
		}
}
	
	public int getClientID(){
		return (int) clientID;
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