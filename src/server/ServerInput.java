package server;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import packetParsers.PacketParser;

public class ServerInput extends Thread{
	private final Socket clientSocket;
	private final PacketParser pp;
	private final int clientID;
	private String roomKey = null;
	
	public ServerInput(Socket cs, int cID){
		clientID = cID;
		clientSocket = cs;
		pp = new PacketParser(clientID);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestingInterface window = new TestingInterface(clientID);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
			Server.closeClient(roomKey, clientID);
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