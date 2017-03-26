package com;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class GrainWidget extends JPanel {
	private static final long serialVersionUID = 1L;
	JLabel lblGrainNum = new JLabel("");
	JSlider sldPlaybackTime = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
	JLabel lblMinLoc = new JLabel("Min");
	JLabel lblMaxLoc = new JLabel("Max");
	JLabel lblGrainSize = new JLabel("Size");
	JTextField txtMinLoc = new JTextField("0", 6);
	JTextField txtMaxLoc = new JTextField("0", 6);
	JTextField txtGrainSize = new JTextField("50", 6);
	JButton btnSetVals = new JButton("Set");
	
	public GrainWidget(int grainNum){
		lblGrainNum.setText(Integer.toString(grainNum));
		sldPlaybackTime.setPreferredSize(new Dimension(800, 20));
		this.add(lblGrainNum);
		this.add(sldPlaybackTime);
		this.add(lblMinLoc);
		this.add(txtMinLoc);
		this.add(lblMaxLoc);
		this.add(txtMaxLoc);
		
	}
}
