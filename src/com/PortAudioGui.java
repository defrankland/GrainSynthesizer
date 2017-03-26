package com;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/***************************************************************************************************************
 * Class PortAudioGui - Defines a panel that displays the main granular synth data
 ****************************************************************************************************************/
public class PortAudioGui extends JPanel implements ActionListener, ChangeListener, MouseListener, 
		MouseMotionListener, ItemListener, Runnable {
	//junk
	private static final long serialVersionUID = -383473214069639043L;
	Integer[] grainNums = new Integer[]{0,1,2};
	
	//control components
	JSlider sldPlaybackTime = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
	JLabel lblMinLoc = new JLabel("Min");
	JLabel lblMaxLoc = new JLabel("Max");
	JLabel lblGrainSize = new JLabel("Size");
	JTextField txtMinLoc = new JTextField("0", 6);
	JTextField txtMaxLoc = new JTextField("0", 6);
	JTextField txtGrainSize = new JTextField("50", 6);
	JCheckBox chkLinkGrains = new JCheckBox("Link Grains?", true);
	JComboBox<Integer> cmbGrainSelect = new JComboBox<>(grainNums);
	JButton btnSetVals = new JButton("Set");
	
	/* File/Sample Properties */
	int fileSz = 0;
	
	/* conditional */
	private boolean _initialized = false;
	
	//panels
	JPanel pnlGrainParams = new JPanel();
	
	/***************************************************************************************************************
	 * Constructor
	 ****************************************************************************************************************/
	public PortAudioGui(int xDim, int yDim){
		this.setPreferredSize(new Dimension(xDim, yDim));
		
		btnSetVals.setActionCommand("SET");
		btnSetVals.addActionListener(this);
		cmbGrainSelect.addItemListener(this);
		sldPlaybackTime.addChangeListener(this);
		
		//slider setup
		//sldPlaybackTime.setPreferredSize(new Dimension(xDim, yDim));
		//sldPlaybackTime.setPaintTicks(true);
		//sldPlaybackTime.setPaintLabels(true);
		//sldPlaybackTime.setMinorTickSpacing(10);
		//sldPlaybackTime.setMajorTickSpacing(40);
        
		//setup the grain parameter panel -- internal to this panel
		pnlGrainParams.add(chkLinkGrains);
		pnlGrainParams.add(new JLabel("Select grain:"));
		pnlGrainParams.add(cmbGrainSelect);
		
		//add components to the this panel
		this.add(sldPlaybackTime);
		this.add(pnlGrainParams);
		this.add(lblMinLoc);
		this.add(txtMinLoc);
		this.add(lblMaxLoc);
		this.add(txtMaxLoc);
		//this.add(btnSetVals);
		
		addMouseListener(this);
		addMouseMotionListener(this);
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
		txtMaxLoc.setText(Long.toString(PlaybackController.grainMaxVal[grainNum]));
		txtMinLoc.setText(Long.toString(PlaybackController.grainMinVal[grainNum]));
		txtGrainSize.setText(Integer.toString(PlaybackController.grainSize));
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int fiLen = new JniLib().getFileLength();
		
		if(e.getActionCommand() == "SET"){
			//new JniLib().setGrainRange((Integer) cmbGrainSelect.getSelectedItem(), Integer.parseInt(txtMinLoc.getText()) , Integer.parseInt(txtMaxLoc.getText()));
		}
	}

	@Override
	public void run() {
		while(true){
			
			if(PlaybackController._running && PlaybackController.isInitialized && !_initialized ){
				//set the controls to the initialized file size
				sldPlaybackTime.setMinimum(0);
				sldPlaybackTime.setMaximum((int)PlaybackController.getFileSize());
				_initialized = true;
				stateChanged(new ChangeEvent(new Object()));
			}
			else if(!PlaybackController._running && _initialized)
			{
				_initialized = false;
			}
			else if(PlaybackController._running && _initialized){
				int state = new JniLib().getGranuleState(0);
			}
			
			
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//variable update
		//PlaybackController.currentPosition = sldPlaybackTime.getValue();
		sldPlaybackTime.setMinimum(0);
		sldPlaybackTime.setMaximum((int)PlaybackController.fileSize);
		//PlaybackController.adjustTimeVars(); 
		
		//GUI update
		verifyMinMaxLocFields((Integer) cmbGrainSelect.getSelectedItem());
		
		//runtime update
		if(!_initialized){
			for(int gn=0; gn<PlaybackController.numVoices; gn++)
			{
				new JniLib().setCurrentPosition(gn, sldPlaybackTime.getValue(), false);
				new JniLib().setGrainRange(gn, Integer.parseInt(txtMinLoc.getText()), Integer.parseInt(txtMaxLoc.getText()));
			}
		}
		new JniLib().setCurrentPosition((Integer) cmbGrainSelect.getSelectedItem(), sldPlaybackTime.getValue(), false);
		new JniLib().setGrainRange((Integer) cmbGrainSelect.getSelectedItem(), Integer.parseInt(txtMinLoc.getText()), Integer.parseInt(txtMaxLoc.getText()));
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
//		Component c = getComponentAt(e.getPoint());
//		
//		if(c == sldPlaybackTime){
//			System.out.println(e.getPoint());
//		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {

		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getStateChange() == ItemEvent.DESELECTED) {
	          Object item = event.getSource();
	          if(item == cmbGrainSelect){
	        	  int gn = (Integer) cmbGrainSelect.getSelectedItem();
	        	  long[] rngVals = new JniLib().getGrainRange(gn);
	        	  PlaybackController.grainMinVal[gn] = rngVals[0];
	        	  PlaybackController.grainMaxVal[gn] = rngVals[1];
	        	  txtMinLoc.setText(Long.toString(PlaybackController.grainMinVal[gn]));
	        	  txtMaxLoc.setText(Long.toString(PlaybackController.grainMaxVal[gn]));
	        	  sldPlaybackTime.setValue((int) (((PlaybackController.grainMaxVal[gn] - PlaybackController.grainMinVal[gn])/2) 
	        			  									+ PlaybackController.grainMinVal[gn]));
	          }
	       }
	}

}
