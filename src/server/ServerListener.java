package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import packets.Packet;

public class ServerListener extends Thread{

	private int portNumber;//port number
	private ServerSocket listener;
	static private final HashMap<Integer, ClientHandler> clients = new HashMap<Integer, ClientHandler>();//various Threads
	private int latestClientID = 0;
	
	ServerListener(){ //setting up the Socket before running the Thread.
		createListener();
	}
	
	@Override
	public void run(){
		try {
			//listening to the socket for clients.
			while(!Thread.currentThread().isInterrupted()){
				Socket clientSocket = null;
				System.out.println("Server: Waiting for a client on the port: "+portNumber);
				clientSocket = listener.accept();
				latestClientID++;
				System.out.println("Server: Client found, passing to ClientHandler" + latestClientID);
				System.out.println(latestClientID);
				if(clientSocket != null){
					clients.put(latestClientID, new ClientHandler(clientSocket, latestClientID));
				} else {
					System.err.println("Unable to use client Socket.");
				}
			}
		} catch (IOException ioe) {
			System.err.println("Server: there was an error connecting to the client. "+ioe.getMessage());
		} finally {
			//closing the socket and thread.
			try {
				if(listener != null){
					listener.close();
				}
				System.err.println("Closing the listener");
				
				Thread.currentThread().interrupt();
				} catch (IOException ioe) {
					System.err.println("Server: There was an error closing the socket. "+ioe.getMessage());
			}
		}
	}
	
	//Creating the ServerSocket.
	private void createListener(){
		boolean found = false;//has a valid port number been found?
		InetAddress addr = null;
		int portNum = 10401;
		boolean selectingIP = true;
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		while(selectingIP){
			System.out.println("Please enter the IP as folows: 'xxx.xxx.xxx.xxx'");
			String input = "";
			try {
				input = inputReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			selectingIP = false;
			try {//attempting to get the localhost IP.
				addr = InetAddress.getByName(input);
			} catch (UnknownHostException e) {
				System.err.println("Invalid IP");
				selectingIP = true;
			}
		}
		System.out.println("IP accepted");
		inputReader = null;
		
		//essentially looping through port numbers from 10401 until a free one is found.
		while(!found){
			try {
				listener = setListener(portNum, addr);
				found = true;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.err.println("Server: Port "+portNum+" unavailable, attempting new port "+(portNum+1));
				found = false;
				portNum++;
			}	
		}
		portNumber = portNum;
	}
	
	//Testing whether the arguments for the Socket will be accepted, if so it returns, otherwise it throws an exception.
	private ServerSocket setListener(int portNum, InetAddress addr) throws Exception{
		ServerSocket temp = null;
		try {
			temp = new ServerSocket(portNum, 0, addr/*InetAddress.getByAddress(new byte[]{(byte)138,(byte)38,(byte)188,(byte)238})*/);
		} catch (IOException ioe) {
			throw new Exception("Failed to secure socket: " + ioe.getMessage());
		}
		return temp;
	}

	public void closeConnections(){
		for(int i = latestClientID; i > 0; i--){
			if(clients.containsKey(i)){
				clients.get(i).close();
				clients.remove(i);
			}
		}
		if(listener != null){
			try {
				listener.close();
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
				System.exit(1);
			}
		}
		Thread.currentThread().interrupt();
	}
	
	public void closeClient(int clientNum){
		if(clients.containsKey(clientNum)){
			clients.get(clientNum).close();
			clients.remove(clientNum);	
		}
	}
	
	public void setRoomKey(int clientID, String key){
		clients.get(clientID).setRoomKey(key);
	}
	
	public void sendPacket(int clientID, Packet serverPacket){
		if(clients.containsKey(clientID)){
			clients.get(clientID).sendPacket(serverPacket);
		}
	}
	
}