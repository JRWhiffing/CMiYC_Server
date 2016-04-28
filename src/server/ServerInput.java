package server;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import packets.GenericPacket;
import packets.Packet;
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
	}
	
	@Override
	public void run() {
		int read = -1;
		byte[] temp = new byte[512];
		try{
//			while((read = clientSocket.getInputStream().read(temp, 0, temp.length)) > -1){
//				byte[] clientPacket = new byte[0];
//				System.out.println("Message Recieved, Processing.");
//				clientPacket = Arrays.copyOfRange(temp, 0, read);
//				GenericPacket gp = new GenericPacket(clientPacket);
//				TestingInterface.ta.append(gp.toString() + "\n------------------------\n");
//				pp.processPacket(clientPacket);
//				temp = new byte[512];
			int length;
			DataInputStream stream = new DataInputStream(clientSocket.getInputStream());
			while(true){
				if((length = stream.readInt()) > 0){
					byte[] data = new byte[length];
                    stream.readFully(data);
					Packet packet = new GenericPacket(data);
					System.out.println(packet.toString() + "\n------------------------\n");
					//TestingInterface.ta.append(packet.toString() + "\n------------------------\n");
					pp.processPacket(data);
					length = 0;
				}
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