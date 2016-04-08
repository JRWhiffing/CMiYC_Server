package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import packetParsers.PacketParser;

public class ServerInput extends Thread{
	private final Socket clientSocket;
	private final PacketParser pp;
	private final int clientID;
	private String roomKey = null;
	
	public ServerInput(Socket cs, int clientID){
		this.clientID = clientID;
		clientSocket = cs;
		pp = new PacketParser(clientID);
	}
	
	@Override
	public void run() {
		int read = -1;
		byte[] temp = new byte[512];
		try{
			while((read = clientSocket.getInputStream().read(temp, 0, temp.length)) > -1){
				byte[] clientPacket = new byte[0];
				System.out.println("Message Recieved, Processing.");
				clientPacket = Arrays.copyOfRange(temp, 0, read);
				pp.processPacket(clientPacket);
				temp = new byte[512];
			}
		} catch (Exception e){
			System.err.println(e.getMessage());
			Server.closeServer();
		} finally {
			Server.quitPlayer(roomKey, clientID);
		}
	}
	
	public void close(){
		if(clientSocket != null){
			if (!clientSocket.isClosed()){
				try {
					clientSocket.close();
				} catch (IOException ioe) {
					System.err.println(ioe.getMessage());
				}
				System.out.println("Input Closing");
			}
		}
		if (!Thread.currentThread().isInterrupted()){
			Thread.currentThread().interrupt();
		}
	}
	
	public void setRoomKey(String key){
		roomKey = key;
		pp.setRoomKey(key);
	}
	
}