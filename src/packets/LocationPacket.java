package packets;

import packets.Packet;

/**
 * Class used for packets concerning a Player's location.
 * Get methods unpacks packets and put methods builds packets
 * @author James and Adam
 *
 */
public class LocationPacket extends Packet {
	
	/**
	 * Constructor that puts the Location Packet ID at the start of the packet array
	 */
	public LocationPacket() {
		putByte(Packet.LOCATION);
	}
	
	/**
	 * Constructor that sets it's argument as the packet array
	 * @param data - Byte Array that contains the packet data
	 */
	public LocationPacket(byte[] data) {
		packet = data;
	}
	
	/**
	 * Method that puts two doubles into the packet
	 * @param longitude - Double that corresponds to the longitude coordinate of the Player
	 * @param latitude - Double that corresponds to the latitude coordinate of the Player
	 */
	public void putLocation(double longitude, double latitude) {
		putDouble(longitude);
		putDouble(latitude);
	}	
	
	/**
	 * Method that extracts two doubles from the packet and returns them
	 * These two doubles correspond to the longitude and latitude location of a Player
	 * @return - The two doubles in an array
	 */
	public double[] getLocation() {
		return new double[]{getDouble(), getDouble()};
	}
	
	public void putID(int ID){
		putInt(ID);
	}
	
	public int getID(){
		return getInt();
	}
	
}