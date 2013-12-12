package com.labeneko.androidgames.framework.gl;

public class Animation {
	//Constant
	public static final int ANIMATION_LOOPING = 0;
	public static final int ANIMATION_NONLOOPING = 1;
	
	//Field
	final TextureArea[] targetFrames;
	final float frameDuration_s;
	
	//Constructor
	public Animation(TextureArea[] targetFrames, float frameDuration_sec) {
		this.targetFrames = targetFrames;
		this.frameDuration_s = frameDuration_sec;
	}
	
	//Method
	public TextureArea getTargetFrame(float stateTime_s, int loopMode) {
		int frameNumber = (int) (stateTime_s / frameDuration_s);
		
		if (loopMode == ANIMATION_NONLOOPING) {
			frameNumber = Math.min(targetFrames.length - 1, frameNumber);
		} else {
			frameNumber = frameNumber % targetFrames.length;
		}
		
		return targetFrames[frameNumber];
	}
}
