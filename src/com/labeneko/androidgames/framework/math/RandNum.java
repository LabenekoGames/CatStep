package com.labeneko.androidgames.framework.math;

import java.util.Random;

public class RandNum {
	//Field
	public static final Random rand = new Random();
	
	//Method
	public static float randF() {
		return rand.nextFloat(); 
	}
}
