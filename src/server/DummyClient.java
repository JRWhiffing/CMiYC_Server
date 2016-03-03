package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import packets.Packet;
import packets.clientPackets.*;
import packets.serverPackets.*;

public class DummyClient {
	
	static Socket temp;
	static private Packet cp;
	
	public static void main(String[] args){
		try {
			temp = new Socket(InetAddress.getByName("138.38.190.177"), 10401);
			System.out.println("Connecting to server");
		} catch (Exception e) {
			System.err.println("There was an issue connecting to the server: "+e.getMessage());
			System.exit(1);
		}
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		// Keep listening forever
		while (true) {
			try {
				// Try to grab a command from the command line
				if(br.ready()){
					final String command = br.readLine();
					// Test for EOF (ctrl-D)
					if (command == null) {
						temp.close();
						System.exit(0);
					}
					if (command.equals("test1")){
//						cp = new ClientPacket();
//						cp.putByte(ClientPacket.JOIN);
//						cp.putString("Hello", 5);
						
						temp.getOutputStream().write(cp.getPacket());
						temp.getOutputStream().flush();
					} else if(command.equals("char")){
//						cp = new ClientPacket();
//						cp.putByte(ClientPacket.LOCATION_ID);
//						cp.putDouble(1000);
//						cp.putDouble(750);
						
						temp.getOutputStream().write(cp.getPacket());
						temp.getOutputStream().flush();
					} else if(command.equals("test2")){
//						cp = new ClientPacket();
//						cp.putByte(ClientPacket.ABILITY_USAGE);
						
						temp.getOutputStream().write(cp.getPacket());
						temp.getOutputStream().flush();
					} else if(command.equals("quit")){
						try {
							temp.close();
						} catch (IOException e) {
							System.err.println("You have been disconnected" + e.getMessage());
						}
					}
				} else {
					if(temp.getInputStream().available() != 0){
						byte[] bytes = new byte[512];
						byte[] serverPacket = new byte[0];
						int read = temp.getInputStream().read(bytes, 0, bytes.length);
						serverPacket = Arrays.copyOfRange(bytes, 0, read);
						//Packet sp = new ServerPacket(serverPacket);
						//System.out.println(sp.toString());
					}
				}
			} catch (final RuntimeException e) {
				// Die if something goes wrong.
				System.err.println(e.getMessage());
				System.exit(1);
			} catch (final IOException e) {
				// Die if something goes wrong.
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
	}
	
}