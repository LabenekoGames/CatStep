package com.labeneko.androidgames.catstep;

import java.util.Random;

import com.labeneko.androidgames.gamemodel2d.DynamicGameModel;

public class Step extends DynamicGameModel {
	//Constant
	public static final float STEP_WIDTH = 2.0f;
	public static final float STEP_HEIGHT = 0.5f;
	public static final int STEP_TYPE_STATIC = 0;
	public static final int STEP_TYPE_MOVING = 1;
	public static final int STEP_STATE_NORMAL = 0;
	public static final int STEP_STATE_BREAKDOWN = 1;
	public static final float STEP_BREAKDOWN_TIME_S = 0.4f * 4;
	public static float STEP_MAX_V = 2.0f * Settings.level;
	
	//Field
	int type;
	int state;
	float step_v_x;
	float stateTime_s;
	public final Random rand;

	//Constructor
	public Step(int type, float wCenterX, float wCenterY) {
		super(wCenterX, wCenterY, STEP_WIDTH, STEP_HEIGHT);
		this.rand = new Random();
		this.type = type;
		this.state = STEP_STATE_NORMAL;
		this.stateTime_s = 0;
		if (type == STEP_TYPE_MOVING) {
			step_v_x = STEP_MAX_V * rand.nextFloat();
			velocity.wX = step_v_x;
		}
	}

	//Method
	public void update(float elapsedTime_s) {
		if (type == STEP_TYPE_MOVING) {
			position.add(velocity.wX * elapsedTime_s, velocity.wY * elapsedTime_s);
			bounds.lowerLeft.set(position).sub(STEP_WIDTH / 2, STEP_HEIGHT / 2);

			if (position.wX < STEP_WIDTH / 2) {
				position.wX = STEP_WIDTH / 2;
				velocity.wX = -velocity.wX;
			}
			if (position.wX > WorldSimulator.WORLD_WIDTH - STEP_WIDTH / 2) {
				position.wX = WorldSimulator.WORLD_WIDTH - STEP_WIDTH / 2;
				velocity.wX = -velocity.wX;
			}
		}
		
		if (state == STEP_STATE_BREAKDOWN) {
			velocity.add(0, WorldSimulator.gravity.wY / 4 * elapsedTime_s * Settings.level);
			position.add(velocity.wX * elapsedTime_s, velocity.wY * elapsedTime_s);
		}
		
		stateTime_s += elapsedTime_s;
	}
	
	public void breakdown() {
		state = STEP_STATE_BREAKDOWN;
		stateTime_s = 0;
		velocity.wX = 0;
	}
 
}
