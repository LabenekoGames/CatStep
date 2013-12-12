package com.labeneko.androidgames.catstep;

import com.labeneko.androidgames.gamemodel2d.DynamicGameModel;

public class Cat extends DynamicGameModel {
	//Constant
	public static final int CAT_STATE_JUMP = 0;
	public static final int CAT_STATE_FALL = 1;
	public static final int CAT_STATE_CRASH = 2;
	
	public static final float CAT_JUMP_VELOCITY = 10.0f;
	public static final float CAT_MOVE_VELOCITY = 20.0f;
	
	public static final float CAT_WIDTH = 1.0f;
	public static final float CAT_HEIGHT = 1.0f;
	
	//Field
	int state;
	float stateTime_s;

	//Constructor
	public Cat(float wCenterX, float wCenterY) {
		super(wCenterX, wCenterY, CAT_WIDTH, CAT_HEIGHT);
		state = CAT_STATE_FALL;
		stateTime_s = 0;
	}

	//Method
	public void update(float elapsedTime_s) {
		velocity.add(WorldSimulator.gravity.wX * elapsedTime_s, WorldSimulator.gravity.wY * elapsedTime_s);
		position.add(velocity.wX * elapsedTime_s, velocity.wY * elapsedTime_s);
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
		if (velocity.wY > 0) {
			if (state != CAT_STATE_CRASH && state != CAT_STATE_JUMP) {
				state = CAT_STATE_JUMP;
				stateTime_s = 0;
			}
		}
		if (velocity.wY < 0) {
			if (state != CAT_STATE_CRASH && state != CAT_STATE_FALL) {
				state = CAT_STATE_FALL;
				stateTime_s = 0;
			}
		}

		stateTime_s += elapsedTime_s;
	}
	
	public void touchRat() {
		velocity.set(0, 0);
		state = CAT_STATE_CRASH;
		stateTime_s = 0;
	}
	
	public void touchStep() {
		velocity.wY = CAT_JUMP_VELOCITY;
		state = CAT_STATE_JUMP;
		stateTime_s = 0;
	}
	
	public void touchSpring() {
		velocity.wY = CAT_JUMP_VELOCITY * 1.5f;
		state = CAT_STATE_JUMP;
		stateTime_s = 0;
	}
}
