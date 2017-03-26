package com;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/***************************************************************************************************************
 * Class PnlGrainControl - Defines a JPanel that displays the main granular synth controls
 ****************************************************************************************************************/
public class PnlGrainControl extends JPanel implements ActionListener, ChangeListener, Runnable {
	private static final long serialVersionUID = 1L;
	
	/*Control components */
	List<GrainWidget> grains = new ArrayList<GrainWidget>();
	JCheckBox chkLinkGrains = new JCheckBox("Auto Link Grains?", false);
	JCheckBox chkManLinkGrains = new JCheckBox("Man Link Grains?", false);
	JTextField txtOverlap = new JTextField("0", 6);
	
	/* conditional */
	private boolean _initialized = false;
	
	public PnlGrainControl(int xDim, int yDim){
		//BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		//this.setLayout(layout);
		
		chkLinkGrains.setActionCommand("LINK");
		chkLinkGrains.addActionListener(this);
		chkManLinkGrains.setActionCommand("MANLINK");
		chkManLinkGrains.addActionListener(this);
		
		this.setPreferredSize(new Dimension(xDim, yDim));
		
		this.add(chkLinkGrains);
		this.add(chkManLinkGrains);
		this.add(new JLabel("Overlap:"));
		this.add(txtOverlap);
		
		for(int i = 0; i< 10; i++){
			grains.add(i, new GrainWidget(i));
			this.add(grains.get(i));
			grains.get(i).sldPlaybackTime.addChangeListener(this);
		}
		
	}
	
	/***************************************************************************************************************
	 * loadPanel()
	 ****************************************************************************************************************/
	public JPanel loadPanel(){
		return this;
	}
	
	
	/***************************************************************************************************************
	 * verifyMinMaxLocFields() - adjust the min and max based on file size
	 ****************************************************************************************************************/
	private void verifyMinMaxLocFields(int grainNum){
		grains.get(grainNum).txtMaxLoc.setText(Long.toString(PlaybackController.getGrainMaxVal(grainNum)));
		grains.get(grainNum).txtMinLoc.setText(Long.toString(PlaybackController.getGrainMinVal(grainNum)));
		grains.get(grainNum).txtGrainSize.setText(Integer.toString(PlaybackController.grainSize));
	}
	
	/***************************************************************************************************************
	 * initSliders() - 
	 ****************************************************************************************************************/
	public void initSliders(int min, int max){
		for(int i = 0; i<grains.size(); i++){
			
			grains.get(i).setVisible(true);
			
			if(i < PlaybackController.numVoices){
				grains.get(i).sldPlaybackTime.setMinimum((int) min);
				grains.get(i).sldPlaybackTime.setMaximum((int) max);
			}
			else{
				grains.get(i).setVisible(false);
			}
		}
	}
	
	
	
	/***************************************************************************************************************
	 * updateSliderPositions() - 
	 ****************************************************************************************************************/
	public void updateSliderPositions(int grainIdx, GrainWidget sender){
		JniLib stk = new JniLib();
		
		//link sliders if selected
		if(PlaybackController._running == true){
			int overlap = Integer.parseInt(txtOverlap.getText());
			if(chkLinkGrains.isSelected()){
				for(int i = 0; i < PlaybackController.numVoices; i++){
					//update sliders & ranges
					int setVal;
					if(i>0){
						setVal = (i * (PlaybackController.grainSize)) + grains.get(0).sldPlaybackTime.getValue() - (overlap*(i-1)) - overlap;
						if(setVal < PlaybackController.fileSize){
							grains.get(i).sldPlaybackTime.setValue(setVal);
						}
						else{
							setVal = (int)PlaybackController.fileSize;
							grains.get(i).sldPlaybackTime.setValue(setVal);
						}
						PlaybackController.adjustTimeVars(setVal, i);
						verifyMinMaxLocFields(i);
					}
					else{
						PlaybackController.adjustTimeVars(grains.get(i).sldPlaybackTime.getValue(), i);
						verifyMinMaxLocFields(i);
					}
					//update audio stream
					//stk.setCurrentPosition(i, grains.get(i).sldPlaybackTime.getValue(), false);
					stk.setGrainRange(i, Integer.parseInt(grains.get(i).txtMinLoc.getText()), 
							Integer.parseInt(grains.get(i).txtMaxLoc.getText()));
				}
			}
			//else sliders not linked
			else{
				PlaybackController.adjustTimeVars(grains.get(grainIdx).sldPlaybackTime.getValue(), grainIdx);
				verifyMinMaxLocFields(grainIdx);
				//stk.setCurrentPosition(grainIdx, sender.sldPlaybackTime.getValue(), false);
				stk.setGrainRange(grainIdx, Integer.parseInt(grains.get(grainIdx).txtMinLoc.getText()), 
						Integer.parseInt(grains.get(grainIdx).txtMaxLoc.getText()));
				
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//linked checkbox
		if(e.getActionCommand() == "LINK"){
			boolean state = chkLinkGrains.isSelected();
			chkManLinkGrains.setSelected(!state);
			chkManLinkGrains.setEnabled(!state);
			for(int i = 1; i < PlaybackController.numVoices; i++){
				grains.get(i).sldPlaybackTime.setEnabled(!state);
			}
		}
		if(e.getActionCommand() == "MANLINK"){
			boolean state = chkManLinkGrains.isSelected();
			chkLinkGrains.setSelected(!state);
			chkLinkGrains.setEnabled(!state);
			for(int i = 1; i < PlaybackController.numVoices; i++){
				grains.get(i).sldPlaybackTime.setEnabled(!state);
			}
		}
	}

	@Override
	public void run() {
		while(true){
			
			if(PlaybackController._running && PlaybackController.isInitialized && !_initialized ){
				//set the slider controls to the initialized file size
				initSliders(0, (int) PlaybackController.getFileSize());
				
				_initialized = true;
				//stateChanged(new ChangeEvent(new Object()));
			}
			else if(!PlaybackController._running && _initialized)
			{
				_initialized = false;
			}
			else{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider sld = (JSlider) e.getSource();
		
		
		for(GrainWidget g: grains){
			if(sld == g.sldPlaybackTime){
				int grainIdx =  grains.indexOf(g);
				GrainWidget sender = grains.get(grainIdx);
				
				//variable update
				//PlaybackController.adjustTimeVars(sender.sldPlaybackTime.getValue(), grainIdx); 
			
				//runtime update
				updateSliderPositions(grainIdx, sender);
			}
		}
	}

}
