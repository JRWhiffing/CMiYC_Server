package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class DummyClient {
	
	static Socket temp;
	static private BufferedReader reader;
	static private PrintWriter writer;
	
	public static void main(String[] args){
		try {
			temp = new Socket(InetAddress.getByName("localhost"), 10401);
			System.out.println("Connecting to server");
			writer = new PrintWriter(temp.getOutputStream());
			reader = new BufferedReader( new InputStreamReader(temp.getInputStream()));
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
						
					} else if(command.equals("char")){
						String data = "";
						data += Byte.toString((byte) 0x02);
						data += Byte.toString((byte) 0x01);
						char[] text = new char[]{'H', 'e', 'l', 'l', 'o'};
						data += new String(text);
						byte[] data_ = getChecksum(data.getBytes());
						temp.getOutputStream().write(data_);
					}
				}				
			} catch (final RuntimeException e) {
				// Die if something goes wrong.
				System.err.println(e.getMessage());
				System.exit(1);
			} catch (final IOException e) {
				// Die if something goes wrong.
				System.err.println(e.toString());
				System.exit(1);
			}
		}
		
	}
	
	private static byte[] getChecksum(byte[] packet){//used to perform a checksum on a packet.
		byte[] newPacket = new byte[packet.length + 1];
		byte checksum = 0x00;
		
		for(int i = 0; i < packet.length; i++){
			checksum += packet[i];
			newPacket[i] = packet[i];//could replace with an Array copy thingy.
		}
		newPacket[newPacket.length - 1] = checksum; //adding the checksum to the packet.
		
		return new byte[0];
	}
	
}