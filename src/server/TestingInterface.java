package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
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
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A graphical interface to send packets to the server (to debug and test the server) and to
 * print responses
 * 
 * @author James
 *
 */

public class TestingInterface implements ActionListener {

	private int clientID;
	private JButton[] serverPackets = new JButton[13];
	private JButton[] lobbyInfoPackets = new JButton[7];
	private JButton[] broadcastPackets = new JButton[8];
	public static JTextArea ta= new JTextArea();
	private JPanel contentPane;
	public JFrame frame;

	/**
	 * Create the frame.
	 */
	public TestingInterface(int clientID) {
		frame = new JFrame("Testing Interface");
		this.clientID = clientID;
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{144, 144, 144, 144, 0};
		gbl_contentPane.rowHeights = new int[]{290, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel serv = new JLabel("Server packets");
		GridBagConstraints gbc_serv = new GridBagConstraints();
		gbc_serv.anchor = GridBagConstraints.NORTH;
		gbc_serv.fill = GridBagConstraints.HORIZONTAL;
		gbc_serv.insets = new Insets(0, 0, 0, 5);
		gbc_serv.gridx = 0;
		gbc_serv.gridy = 0;
		contentPane.add(serv, gbc_serv);
		for(int i = 0; i < 13; i++){
			serverPackets[i] = new JButton(""+i);
			serverPackets[i].setPreferredSize(new Dimension(70, 70));
			serverPackets[i].addActionListener(this);
			gbc_serv.gridy = i + 1;
			contentPane.add(serverPackets[i], gbc_serv);
		}
		
		JLabel lobinf = new JLabel("Lobby packets");
		GridBagConstraints gbc_lobinf = new GridBagConstraints();
		gbc_lobinf.anchor = GridBagConstraints.NORTH;
		gbc_lobinf.fill = GridBagConstraints.HORIZONTAL;
		gbc_lobinf.insets = new Insets(0, 0, 0, 5);
		gbc_lobinf.gridx = 1;
		gbc_lobinf.gridy = 0;
		contentPane.add(lobinf, gbc_lobinf);
		for(int i = 0; i < 7; i++){
			lobbyInfoPackets[i] = new JButton(""+i);
			lobbyInfoPackets[i].setPreferredSize(new Dimension(70, 70));
			lobbyInfoPackets[i].addActionListener(this);
			gbc_lobinf.gridy = i + 1;
			contentPane.add(lobbyInfoPackets[i], gbc_lobinf);
		}
		
		JLabel broad = new JLabel("Broadcast packets");
		GridBagConstraints gbc_broad = new GridBagConstraints();
		gbc_broad.insets = new Insets(0, 0, 0, 5);
		gbc_broad.anchor = GridBagConstraints.NORTH;
		gbc_broad.fill = GridBagConstraints.HORIZONTAL;
		gbc_broad.gridx = 2;
		gbc_broad.gridy = 0;
		contentPane.add(broad, gbc_broad);
		for(int i = 0; i < 8; i++){
			broadcastPackets[i] = new JButton(""+i);
			broadcastPackets[i].setPreferredSize(new Dimension(70, 70));
			broadcastPackets[i].addActionListener(this);
			gbc_broad.gridy = i + 1;
			contentPane.add(broadcastPackets[i], gbc_broad);
		}
		ta.setEditable(false);
		ta.setWrapStyleWord(true);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 3;
		gbc_scrollPane.gridy = 0;
		contentPane.add(ta, gbc_scrollPane);
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
			System.out.println(new int[]{1337}.length);
			TargetPacket tp = new TargetPacket();
			tp.putTargetID(new int[]{1337});
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
			System.out.println("Testing".length());
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
