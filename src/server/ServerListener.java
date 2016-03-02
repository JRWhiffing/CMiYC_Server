package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import packets.Packet;

public class ServerListener extends Thread{

	private int portNumber;//port number
	private ServerSocket listener;
	private final HashMap<Integer, ClientHandler> clients = new HashMap<Integer, ClientHandler>();//various Threads
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
				System.out.println("Server: Client found, passing to ClientHandler.");
				latestClientID++;
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
		
		try {//attempting to get the localhost IP.
			addr = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("InetAddress unresolved for local host");
			System.exit(1);
		}
		
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
			temp = new ServerSocket(portNum, 0, addr);
		} catch (IOException ioe) {
			throw new Exception("Failed to secure socket: " + ioe.getMessage());
		}
		return temp;
	}

	public void closeConnections(){
		for(int i = latestClientID; i > 0; i--){
			if(clients.containsKey(i)){
				clients.get(i).close();
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
	
	public void closeThread(int clientNum){
		clients.get(clientNum).close();
		
	}
	
	public void setRoomKey(int clientID, String key){
		clients.get(clientID).setRoomKey(key);
	}
	
	public void sendPacket(int clientID, Packet serverPacket){
		System.out.println("Still Going");
		if(clients.containsKey(clientID)){
			System.out.println("Client Thread Found");
			clients.get(clientID).sendPacket(serverPacket);
		}
	}
	
}