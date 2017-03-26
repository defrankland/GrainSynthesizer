package com;

public class JniLib {
	static {  
        // load library   
		try{
        System.loadLibrary("libLibStk");  
		}catch(UnsatisfiedLinkError e) {
		      System.err.println("Native code library failed to load.\n" + e);
		      System.exit(1);
		}
	}
	 // native method signatures
	public native int getCurrentPosition(int grainNumber);
	public native void setCurrentPosition(int grainIndex, int newPosition, boolean setAll);
	public native int getFileLength();
	public native long[] getGrainRange(int grainNumber);
	public native void setGrainRange(int grainIndex, int firstFrame, int lastFrame);
	public native void startGrainSynth(String fName, int numVoices, int grainLen, int rampPct, int offset, int delay, int timeStretch, float rndm);
	public native void stopGrainSynth();
	public native void startRunning();
	public native void stopRunning();
	public native int getGranuleState(int grainIndex);
	
	
}
