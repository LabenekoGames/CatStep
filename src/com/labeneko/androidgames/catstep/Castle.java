package com.labeneko.androidgames.catstep;

import com.labeneko.androidgames.gamemodel2d.GameModel;

public class Castle extends GameModel {
	//Constant
	public static float CASTLE_WIDTH = 2.0f;
	public static float CASTLE_HEIGHT = 2.0f;
	
	//Constructor
	public Castle(float wCenterX, float wCenterY) {
		super(wCenterX, wCenterY, CASTLE_WIDTH, CASTLE_HEIGHT);
	}

}
