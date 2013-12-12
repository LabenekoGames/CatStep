package com.labeneko.androidgames.catstep;

import com.labeneko.androidgames.gamemodel2d.GameModel;

public class Cheese extends GameModel {
	//Constant
	public static final float CHEESE_WIDTH = 0.8f;
	public static final float CHEESE_HEIGHT = 0.8f;
	public static final int CHEESE_SCORE = 10;
	
	//Field
	float stateTime_s;
	
	//Constructor
	public Cheese(float wCenterX, float wCenterY) {
		super(wCenterX, wCenterY, CHEESE_WIDTH, CHEESE_HEIGHT);
		stateTime_s = 0;
	}
	
	//Method
	public void update(float elapsedTime_s) {
		stateTime_s += elapsedTime_s;
	}

}
