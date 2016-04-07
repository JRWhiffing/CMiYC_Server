package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import packets.serverPackets.*;
import packets.serverPackets.broadcastPackets.*;
import packets.serverPackets.lobbyInfoPackets.*;

/**
 * A graphical interface to send packets to the server (to debug and test the server) and to
 * print responses
 * 
 * @author James
 *
 */

public class TestingInterface extends JFrame implements ActionListener {

	private int clientID;
	private JButton[] serverPackets = new JButton[13];
	private JButton[] lobbyInfoPackets = new JButton[7];
	private JButton[] broadcastPackets = new JButton[8];
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public TestingInterface(int clientID) {
		this.clientID = clientID;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		createMenu();
	}
	
	private void createMenu(){
		GridBagConstraints gbcSettings = new GridBagConstraints();
		gbcSettings.anchor = GridBagConstraints.NORTH;
		gbcSettings.fill = GridBagConstraints.BOTH;
		gbcSettings.weightx = 1;
		gbcSettings.weighty = 1;
		JLabel serv = new JLabel("Server packets");
		serv.add(contentPane, gbcSettings);
		for(int i = 0; i < 13; i++){
			serverPackets[i] = new JButton(""+i);
			serverPackets[i].setPreferredSize(new Dimension(70, 70));
			gbcSettings.gridheight = 10;
			gbcSettings.gridwidth = i * 11;
			serverPackets[i].add(contentPane, gbcSettings);
		}
		JLabel lobinf = new JLabel("Lobby packets");
		lobinf.add(contentPane, gbcSettings);
		for(int i = 0; i < 7; i++){
			lobbyInfoPackets[i] = new JButton(""+i);
			lobbyInfoPackets[i].setPreferredSize(new Dimension(70, 70));
			gbcSettings.gridheight = 80;
			gbcSettings.gridwidth = i * 11;
			lobbyInfoPackets[i].add(contentPane, gbcSettings);
		}
		JLabel broad = new JLabel("Broadcast packets");
		broad.add(contentPane, gbcSettings);
		for(int i = 0; i < 8; i++){
			broadcastPackets[i] = new JButton(""+i);
			broadcastPackets[i].setPreferredSize(new Dimension(70, 70));
			gbcSettings.gridheight = 170;
			gbcSettings.gridwidth = i * 11;
			broadcastPackets[i].add(contentPane, gbcSettings);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == serverPackets[0]){
			packets.LocationPacket lp =  new packets.LocationPacket();
			lp.putLocation(10401, 40104);
			Server.sendPacket(clientID, lp);
		} else if(e.getSource() == serverPackets[1]){
			PingPacket pp =  new PingPacket();
			Server.sendPacket(clientID, pp);
		} else if(e.getSource() == serverPackets[2]){
			TargetPacket tp = new TargetPacket();
			tp.putTargetID(new int[1337]);
			Server.sendPacket(clientID, tp);
		} else if(e.getSource() == serverPackets[3]){
			SpawnRegionPacket srp = new SpawnRegionPacket();
			srp.putSpawnPoint(5678, 1234);
			Server.sendPacket(clientID, srp);
		} else if(e.getSource() == serverPackets[4]){
			AbilityUsagePacket aup = new AbilityUsagePacket();
			aup.putAbility((byte)0x03);
			Server.sendPacket(clientID, aup);
		} else if(e.getSource() == serverPackets[5]){
			GameStartPacket gsp = new GameStartPacket();
			Server.sendPacket(clientID, gsp);
		} else if(e.getSource() == serverPackets[6]){
			GameEndPacket gep = new GameEndPacket();
			Server.sendPacket(clientID, gep);
		} else if(e.getSource() == serverPackets[7]){
			RoomClosePacket rcp = new RoomClosePacket();
			Server.sendPacket(clientID, rcp);
		} else if(e.getSource() == serverPackets[8]){
			RoomKeyPacket rkp = new RoomKeyPacket();
			rkp.putRoomKey("Testing");
			Server.sendPacket(clientID, rkp);
		} else if(e.getSource() == serverPackets[9]){
			KickPacket kp = new KickPacket();
			kp.putKickReason((byte)0x01);
			Server.sendPacket(clientID, kp);
		} else if(e.getSource() == serverPackets[10]){
			NAKPacket nakp = new NAKPacket();
			nakp.setNAK((byte)0x01);
			Server.sendPacket(clientID, nakp);
		} else if(e.getSource() == serverPackets[11]){
			HostPacket hp = new HostPacket();
			Server.sendPacket(clientID, hp);
		} else if(e.getSource() == serverPackets[12]){
			CaughtPacket cp = new CaughtPacket();
			Server.sendPacket(clientID, cp);
		} else if(e.getSource() == broadcastPackets[0]){
			
		} else if(e.getSource() == broadcastPackets[1]){
			
		} else if(e.getSource() == broadcastPackets[2]){
			
		} else if(e.getSource() == broadcastPackets[3]){
			
		} else if(e.getSource() == broadcastPackets[4]){
			
		} else if(e.getSource() == broadcastPackets[5]){
			
		} else if(e.getSource() == broadcastPackets[6]){
			
		} else if(e.getSource() == broadcastPackets[7]){
			
		} else if(e.getSource() == lobbyInfoPackets[0]){
			
		} else if(e.getSource() == lobbyInfoPackets[1]){
			
		} else if(e.getSource() == lobbyInfoPackets[2]){
			
		} else if(e.getSource() == lobbyInfoPackets[3]){
			
		} else if(e.getSource() == lobbyInfoPackets[4]){
			
		} else if(e.getSource() == lobbyInfoPackets[5]){
			
		} else if(e.getSource() == lobbyInfoPackets[6]){
			
		}
	}

}
