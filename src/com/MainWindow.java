package com;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow implements ActionListener{
	
	JFrame mainFrame = new JFrame("Granular Synthesis");
	JPanel mainPanel = new JPanel();
	PnlParameters pnlParams;
	PlayControlPanel playCtrlPnl;
	Thread paGuiUpdate;
	
	PortAudioGui paGui;
	PnlGrainControl grainControlWindow;
	
	Properties p = new Properties();
    
	int mainFrameXDim = 1200;
	int mainFrameYDim = 1200;
	int mainPanelXDim = mainFrameXDim;
	int mainPanelYDim = 400;
	int playCtrlPanelXDim = mainFrameXDim;
	int playCtrlPanelYDim = mainFrameYDim - mainPanelYDim;
	
	public static void main(final String[] args) {
		MainWindow mw = new MainWindow(); 
		mw.setupGui();
	}
	
	public void setupGui(){
		
		try {
			p.load(new FileInputStream("user.ini"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//initialize all panels
		//paGui = new PortAudioGui(mainPanelXDim/4, mainPanelYDim);
		grainControlWindow = new PnlGrainControl(mainPanelXDim, mainPanelYDim);
		playCtrlPnl = new PlayControlPanel(mainPanelXDim, 50);
		pnlParams = new PnlParameters(mainPanelXDim, mainPanelYDim);
		
		pnlParams.setVisible(false);
		
		playCtrlPnl.btnPlay.setActionCommand("PLAY");
		playCtrlPnl.btnStop.setActionCommand("STOP");
		playCtrlPnl.btnSetup.setActionCommand("SETUP");
		pnlParams.btnOpenFile.setActionCommand("LOAD_FILE");
		playCtrlPnl.btnPlay.addActionListener(this);
		playCtrlPnl.btnStop.addActionListener(this);
		playCtrlPnl.btnSetup.addActionListener(this);
		pnlParams.btnOpenFile.addActionListener(this);
		
		//add a panel to the main panel
		//mainPanel = paGui.loadPanel();
		mainPanel = grainControlWindow.loadPanel();
		
		//add panels, etc to the mainFrame
		mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainFrame.getContentPane().add(playCtrlPnl, BorderLayout.SOUTH);
		
		
		//configure the main panel
		mainFrame.getGlassPane().setVisible(true);	
		mainFrame.setPreferredSize(new Dimension(mainPanelXDim, mainPanelYDim));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
		//Start threads for controlling GUI components
		paGuiUpdate = new Thread(grainControlWindow, "paGuiUpdate");
		paGuiUpdate.start();
	}

	public void toggleSetupScrn(){
		if(pnlParams.isVisible() == false){
			//pnlParams = new PnlParameters(mainPanelXDim, mainPanelYDim);
			//pnlParams.btnOpenFile.setActionCommand("LOAD_FILE");
			//pnlParams.btnOpenFile.addActionListener(this);
			//mainFrame.remove(mainPanel);
			refreshSetupScrn();
			mainPanel = pnlParams.loadPanel();
			//paGui.setVisible(false);
			grainControlWindow.setVisible(false);
			pnlParams.setVisible(true);
			mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			mainFrame.pack();
			if(PlaybackController._running){
				PlaybackController._running = false;
				new JniLib().stopGrainSynth();
			}
			playCtrlPnl.btnSetup.setText("Return");
			
			
		}
		else{
			//paGui = new PortAudioGui(mainPanelXDim/4, mainPanelYDim);
			//grainControlWindow = new PnlGrainControl(mainPanelXDim, mainPanelYDim);
			//mainFrame.remove(mainPanel);
			mainPanel = grainControlWindow.loadPanel();
			//paGui.setVisible(true);
			grainControlWindow.setVisible(true);
			pnlParams.setVisible(false);
			mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			mainFrame.pack();
			
			enterSetupParams();
			playCtrlPnl.btnSetup.setText("Setup");
			
			//restart if playing back
			if(!PlaybackController._running){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PlaybackController.runPlayback();
			}
		}
	}
	
	public void refreshSetupScrn(){
		pnlParams.txtParams[0].setText(Integer.toString(PlaybackController.numVoices));
		pnlParams.txtParams[1].setText(Integer.toString(PlaybackController.grainLen));
		pnlParams.txtParams[2].setText(Integer.toString(PlaybackController.rampPercent));
		pnlParams.txtParams[3].setText(Integer.toString(PlaybackController.grainOffset));
		pnlParams.txtParams[4].setText(Integer.toString(PlaybackController.grainOffset));
	}
	public void enterSetupParams(){
		PlaybackController.numVoices   = Integer.parseInt(pnlParams.txtParams[0].getText());
		PlaybackController.grainLen   = Integer.parseInt(pnlParams.txtParams[1].getText());
		PlaybackController.rampPercent = Integer.parseInt(pnlParams.txtParams[2].getText());
		PlaybackController.grainOffset = Integer.parseInt(pnlParams.txtParams[3].getText());
		PlaybackController.grainOffset = Integer.parseInt(pnlParams.txtParams[4].getText());
		PlaybackController.filePath = pnlParams.txtfilePath.getText();
		PlaybackController.reCalcGrainSize();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand() == "PLAY"){ 
			if(!PlaybackController._running){
				PlaybackController.runPlayback();
			}
		}
		else if(ae.getActionCommand() == "STOP"){
			PlaybackController._running = false;
			new JniLib().stopGrainSynth();
		}
		else if(ae.getActionCommand() == "SETUP"){
				toggleSetupScrn();
		}
		else if(ae.getActionCommand() == "LOAD_FILE"){
			String defDir = (String) p.get("LastFileDir");
			JFileChooser fcFiPath = new JFileChooser(defDir);
			FileFilter imageFilter = new FileNameExtensionFilter("Wav files", "wav");
			fcFiPath.setFileFilter(imageFilter);
			
			int returnVal =  fcFiPath.showOpenDialog(new JFrame());
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fcFiPath.getSelectedFile();
	            pnlParams.txtfilePath.setText(file.getAbsolutePath());
	            
	            String filePath = file.getAbsolutePath().
	    	    	     substring(0,file.getAbsolutePath().lastIndexOf(File.separator));
	            
	            p.put("LastFileDir", filePath);
				try {
					FileOutputStream out = new FileOutputStream("user.ini");
					p.save(out, "");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	        } 
		}
		
	}

}
