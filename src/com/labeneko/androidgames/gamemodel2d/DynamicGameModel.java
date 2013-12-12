package com.labeneko.androidgames.gamemodel2d;

import com.labeneko.androidgames.framework.math.Vector2d;

public class DynamicGameModel extends GameModel {
	//Field
	public final Vector2d velocity;
	public final Vector2d accel;
	
	//Constructor
	public DynamicGameModel(float wCenterX, float wCenterY, float width, float height) {
		super(wCenterX, wCenterY, width, height);
		velocity = new Vector2d();
		accel = new Vector2d();
	}

}
