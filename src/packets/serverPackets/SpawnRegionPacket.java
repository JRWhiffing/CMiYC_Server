package packets.serverPackets;

import packets.Packet;

public class SpawnRegionPacket extends Packet {

	public SpawnRegionPacket(){
		putByte(Packet.SPAWN_REGION);
	}
	
	public SpawnRegionPacket(byte[] data){
		packet = data;
	}
	
	public void putSpawnPoint(double longitude, double latitude){
		putDouble(longitude);
		putDouble(latitude);
	}
	
	public double[] getSpawnPoint(){
		return new double[]{getDouble(), getDouble()};
	}
	
}