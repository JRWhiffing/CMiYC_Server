package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler extends Thread{
	private final StateMachine sm;
	private final Socket clientSocket;
	private BufferedReader reader;//might not need if sending bytes rather than strings
	private PrintWriter writer;
	private final Integer clientID;
	
	ClientHandler(Socket cs, Integer clientID){
		this.clientID = clientID;
		sm = new StateMachine(clientID);
		clientSocket = cs;
		try{
			// Wrap the input stream in a BufferedReader. (Potentially unneeded)
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			// Wrap the output stream in a BufferedWriter.
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch(IOException ioe){
            try{
            	throw new Exception("IOException talking to the client: "+
                    ioe.getMessage());
            } catch (Exception e){
            	System.err.println("There was an error throwing an exception, not sure what to do now. :(");
            }
		}
	}

	@Override
	public void run() {
		int read = -1;
		byte[] clientPacket = new byte[0];
		byte[] temp = new byte[512];
		try{
			while((read = clientSocket.getInputStream().read(temp, 0, temp.length)) > -1){
				clientPacket = Arrays.copyOfRange(temp, 0, read);
				sendPacket(sm.stateMachine(checkPacket(clientPacket)));
				clientPacket = new byte[0];
			}
		} catch (Exception e){
			System.err.println(e.getMessage());
			Server.closeServer();
		}
	}
	
	private void sendPacket(byte[] serverPacket){
		
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
		if(checksum == checksum_)newPacket[0] = 1; else newPacket[0] = 0;
		
		return newPacket;
	}
	
	public void close(){
		//code to close Thread
		if(clientSocket != null){
			try {
				clientSocket.close();
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
			}
		}
	}
	
}