package com;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PnlParameters extends JPanel{
	private static final long serialVersionUID = 1L;
	
	JLabel[] lblParams = new JLabel[5];
	JTextField[] txtParams = new JTextField[5];
	JTextField txtfilePath = new JTextField(PlaybackController.filePath,80);
	JButton btnOpenFile = new JButton("...");
	
	public PnlParameters(int xDim, int yDim){
		
		this.setPreferredSize(new Dimension(xDim, yDim));
		
		for(int i = 0; i < txtParams.length; i++)
		{
			lblParams[i] = new JLabel();
			txtParams[i] = new JTextField("", 6);
		}
		
		lblParams[0].setText("nVoices");
		lblParams[1].setText("Grain Time");
		lblParams[2].setText("Ramp Percent");
		lblParams[3].setText("Offset");
		lblParams[4].setText("Delay");
		
		//add junk to the main panel - behind other panels
		for(int i=0; i<txtParams.length; i++){
			this.add(lblParams[i]);
			this.add(txtParams[i]);
		}
		this.add(txtfilePath);
		this.add(btnOpenFile);
	}
	
	/***************************************************************************************************************
	 * loadPanel()
	 ****************************************************************************************************************/
	public JPanel loadPanel(){
		return this;
	}
}
