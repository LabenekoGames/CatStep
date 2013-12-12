package com.labeneko.androidgames.catstep;

import java.util.Random;

import com.labeneko.androidgames.gamemodel2d.DynamicGameModel;

public class Fish extends DynamicGameModel {
	//Constant
	public static final float FISH_WIDTH = 1.0f;
	public static final float FISH_HEIGHT = 1.0f;
	public static final float FISH_MAX_V_X = 2.0f * Settings.level;
	public static final float FISH_MAX_V_Y = -2.0f * Settings.level;
	public static final int FISH_SCORE = 30;
	float fish_v_x;
	float fish_v_y;
	float stateTime_s = 0;
	
	//Field
	public final Random rand;

	//Constructor
	public Fish(float wCenterX, float wCenterY) {
		super(wCenterX, wCenterY, FISH_WIDTH, FISH_HEIGHT);
		this.rand = new Random();
		fish_v_x = FISH_MAX_V_X * rand.nextFloat();
		fish_v_y = FISH_MAX_V_Y * rand.nextFloat();
		velocity.set(fish_v_x, fish_v_y);
	}

	//Method
	public void update(float elapsedTime_s) {
		position.add(velocity.wX * elapsedTime_s, velocity.wY * elapsedTime_s);
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
		if (position.wX < FISH_WIDTH / 2) {
			position.wX = FISH_WIDTH / 2;
			velocity.wX = -velocity.wX;
		}
		if (position.wX > WorldSimulator.WORLD_WIDTH - FISH_WIDTH / 2) {
			position.wX = WorldSimulator.WORLD_WIDTH - FISH_WIDTH / 2;
			velocity.wX = -velocity.wX;
		}
		
		if (position.wY < FISH_HEIGHT / 2) {
			position.wY = FISH_HEIGHT / 2;
			velocity.wY = -velocity.wY;
		}
		if (position.wY > WorldSimulator.WORLD_HEIGHT - FISH_HEIGHT / 2) {
			position.wY = WorldSimulator.WORLD_HEIGHT - FISH_HEIGHT / 2;
			velocity.wY = -velocity.wY;
		}
		
		stateTime_s += elapsedTime_s;
	}

}
