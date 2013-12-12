package com.labeneko.androidgames.gamemodel2d;

import com.labeneko.androidgames.framework.math.Rectangle;
import com.labeneko.androidgames.framework.math.Vector2d;

public class GameModel {
	//Field
	public final Vector2d position;
	public final Rectangle bounds;
	
	//Constructor
	public GameModel(float wCenterX, float wCenterY, float width, float height) {
		this.position = new Vector2d(wCenterX, wCenterY);
		this.bounds = new Rectangle(wCenterX-width/2, wCenterY-height/2, width, height);
	}

}
