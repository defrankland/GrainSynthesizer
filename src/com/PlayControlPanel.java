package com;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PlayControlPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 5255356883465293911L;
	JButton btnPlay = new JButton("Play");
	JButton btnStop = new JButton("Stop");
	JButton btnSetup = new JButton("Setup");
	
	public PlayControlPanel(int xDim, int yDim){
		//Button setup
//		btnPlay.setActionCommand("PLAY");
//		btnStop.setActionCommand("STOP");
//		btnSetup.setActionCommand("SETUP");
//		btnPlay.addActionListener(this);
//		btnStop.addActionListener(this);
//		btnSetup.addActionListener(this);
		
		//setup panel
		this.setPreferredSize(new Dimension(xDim, yDim));
		
		//add components to the panel
		this.add(btnPlay);
		this.add(btnStop);
		this.add(btnSetup);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
//		if(ae.getActionCommand() == "PLAY"){ 
//			if(!PlaybackController._running){
//				PlaybackController.runPlayback();
//			}
//		}
//		else if(ae.getActionCommand() == "STOP"){
//			PlaybackController._running = false;
//			new JniLib().stopGrainSynth();
//		}
//		else if(ae.getActionCommand() == "SETUP"){
//			if(PlaybackController._running == false){
//				
//			}
//		}
	}

}
