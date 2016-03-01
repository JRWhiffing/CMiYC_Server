package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import server.stateMachines.StateMachine;

public class ServerInput extends Thread{
	private final Socket clientSocket;
	private final StateMachine sm;
	
	public ServerInput(Socket cs, int clientID){
		clientSocket = cs;
		sm = new StateMachine(clientID);
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
				sm.processPacket(clientPacket);
				temp = new byte[512];
			}
		} catch (Exception e){
			System.err.println(e.getMessage());
			Server.closeServer();
		} finally {
			close();
		}
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