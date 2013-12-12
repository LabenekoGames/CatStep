package com.labeneko.androidgames.catstep;

import java.util.Random;

import com.labeneko.androidgames.gamemodel2d.DynamicGameModel;

public class Rat extends DynamicGameModel {
	//Constant
	public static final float RAT_WIDTH = 1.0f;
	public static final float RAT_HEIGHT = 1.0f;
	public static final float RAT_MAX_V_X = 3.0f * Settings.level;
	public static final float RAT_V_Y = 6.0f;
	
	//Field
	public final Random rand;
	float rat_v_x;
	float stateTime_s = 0;

	//Constructor
	public Rat(float wCenterX, float wCenterY) {
		super(wCenterX, wCenterY, RAT_WIDTH, RAT_HEIGHT);
		this.rand = new Random();
		rat_v_x = RAT_MAX_V_X * rand.nextFloat();
		velocity.set(rat_v_x, 0);
	}

	//Method
	public void update(float elapsedTime_s) {
		velocity.add(0, WorldSimulator.gravity.wY * elapsedTime_s);
		position.add(velocity.wX * elapsedTime_s, velocity.wY * elapsedTime_s);
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
		if (velocity.wY < -3.0f) {
			velocity.add(0, RAT_V_Y);
		}

		if (position.wX < RAT_WIDTH / 2) {
			position.wX = RAT_WIDTH / 2;
			velocity.wX = -velocity.wX;
		}
		if (position.wX > WorldSimulator.WORLD_WIDTH - RAT_WIDTH / 2) {
			position.wX = WorldSimulator.WORLD_WIDTH - RAT_WIDTH / 2;
			velocity.wX = -velocity.wX;
		}

		stateTime_s += elapsedTime_s;
	}

}
