package com;

public final class PlaybackController extends Thread{
	public static boolean _running = false;
	public static boolean isInitialized = false;
	
	//runtime data
	static int sampleRate = 44100;
	static long fileSize = 0;
	static int numVoices = 3;
	static int grainLen = 800; //ms
	static int grainSize = (int) ((grainLen * 0.001) * (sampleRate / numVoices)); //samples
	static long[] grainMinVal = new long[10];
	static long[] grainMaxVal = new long[10];
	static String filePath = "C:\\Users\\Dan\\Desktop\\TestWavs\\tst\\whistle.wav";
	static int rampPercent = 40;
	static int grainOffset = 0;
	static int grainDelay = 0;
	static int timeStretch = 1;
	static float randomizationFactor = 0.0f;
	
	public static long getFileSize(){
		return fileSize;
	}
	
	public static int getGrainSize(){
		return grainSize;
	}
	public static void setGrainSize(int newSz){
		grainSize = newSz;
		//do something to set the new grain size
	}
	public static void reCalcGrainSize(){
		grainSize = (int) ((grainLen * 0.001) * (sampleRate / numVoices));
	}
	
	public static long getGrainMinVal(int grainIndex){
		return grainMinVal[grainIndex];
	}
//	public static void setGrainMinVal(int newVal){
//		grainMinVal = newVal;
//	}
	
	public static long getGrainMaxVal(int grainIndex){
		return grainMaxVal[grainIndex];
	}
//	public static void setGrainMaxVal(int newVal){
//		grainMaxVal = newVal;
//	}
	
	public static void runPlayback(){
		//PlaybackController pb = new PlaybackController();
	}
	
	/***************************************************************************************************************
	 * adjustTimeVars
	 ****************************************************************************************************************/
	public static void adjustTimeVars(int currVal, int grainIdx){

		if(currVal - (PlaybackController.grainSize/2) > 0){
			PlaybackController.grainMinVal[grainIdx] = currVal - (PlaybackController.grainSize/2);
		}
		else{
			PlaybackController.grainMinVal[grainIdx] = 0;
		}
		if(currVal + (PlaybackController.grainSize/2) < PlaybackController.fileSize){
			PlaybackController.grainMaxVal[grainIdx] = currVal + (PlaybackController.grainSize/2);
		}
		else{
			PlaybackController.grainMaxVal[grainIdx] = PlaybackController.fileSize;
			PlaybackController.grainMinVal[grainIdx] = PlaybackController.fileSize - grainSize;
		}
	}
	
		
	private PlaybackController(){
		this.start();
	}
	
	@Override
	public void run() {
		if(!_running){
			new SubFn();
			launchTask();
		}
	}
	
	public void launchTask(){
		Thread updateThr = new Thread(new SubFn(), "updateThr");
		updateThr.start();
		_running = true;
		new JniLib().startGrainSynth(filePath, numVoices, grainLen, 
									rampPercent, grainOffset, grainDelay, timeStretch, randomizationFactor);
		isInitialized = false;
	}
	
	
	private class SubFn extends Thread{
		JniLib stk = new JniLib();
		
		private SubFn(){
			
		}
		
		@Override 
		public void run(){
			while(_running)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//initialize data if needed
				if(!isInitialized && _running){
					isInitialized = true;
					fileSize = stk.getFileLength();
				}
			}
		}
	}
}
