package com.labeneko.androidgames.catstep;

import com.labeneko.androidgames.gamemodel2d.GameModel;

public class Spring extends GameModel {
	//Constant
	public static float SPRING_WIDTH = 0.8f;
	public static float SPRING_HEIGHT = 0.8f;
	
	//Constructor
	public Spring(float wCenterX, float wCenterY) {
		super(wCenterX, wCenterY, SPRING_WIDTH, SPRING_HEIGHT);
	}
}
