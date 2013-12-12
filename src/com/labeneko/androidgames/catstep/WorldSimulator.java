package com.labeneko.androidgames.catstep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.labeneko.androidgames.framework.math.CollisionChecker;
import com.labeneko.androidgames.framework.math.Vector2d;

public class WorldSimulator {
	//Inner interface
	public interface WorldListener {
		public void jump();
		public void highjump();
		public void crash();
		public void get();
	}
	
	//Constant
	public static final float WORLD_WIDTH = 10.0f;
	public static final float WORLD_HEIGHT = 15.0f * 20.0f;
	public static final int WORLD_STATE_PLAYING = 0;
	public static final int WORLD_STATE_NEXT_STAGE = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2d gravity = new Vector2d(0, -9.8f);
	
	//Field
	public final Cat cat;
	public final List<Step> stepList;
	public final List<Spring> springList;
	public final List<Rat> ratList;
	public final List<Cheese> cheeseList;
	public final List<Fish> fishList;

	public Castle castle;
	public final WorldListener worldListener;
	public final Random rand;
	
	public float highestHeight;
	public int stageScore;
	public int worldState;
	
	//Constructor
	public WorldSimulator(WorldListener worldListener) {
		this.cat = new Cat(5, 1);
		this.stepList = new ArrayList<Step>();
		this.springList = new ArrayList<Spring>();
		this.ratList = new ArrayList<Rat>();
		this.cheeseList = new ArrayList<Cheese>();
		this.fishList = new ArrayList<Fish>();
		this.worldListener = worldListener;
		this.rand = new Random();
		
		generateStage();
		
		this.highestHeight = 0;
		this.stageScore = 0;
		this.worldState = WORLD_STATE_PLAYING;
	}
	
	//Method
	private void generateStage() {
		float wScrollY = Step.STEP_HEIGHT / 2;
		float maxJumpHeight = Cat.CAT_JUMP_VELOCITY * Cat.CAT_JUMP_VELOCITY / (2 * -gravity.wY);
		
		while (wScrollY < WORLD_HEIGHT - WORLD_WIDTH / 2) {
			//Generate Step
			int stepType = rand.nextFloat() > 0.8f ? Step.STEP_TYPE_MOVING : Step.STEP_TYPE_STATIC;
			float wScrollX = rand.nextFloat() * (WORLD_WIDTH - Step.STEP_WIDTH) + Step.STEP_WIDTH / 2;
			Step step = new Step(stepType, wScrollX, wScrollY);
			stepList.add(step);
			
			//Generate Spring
			if (stepType != Step.STEP_TYPE_MOVING && rand.nextFloat() > 0.9f) {
				Spring spring = new Spring(step.position.wX, step.position.wY + Step.STEP_HEIGHT / 2 + Spring.SPRING_HEIGHT / 2);
				springList.add(spring);
			}
			
			//Generate Rat
			if (wScrollY > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f) {
				Rat rat = new Rat(step.position.wX + rand.nextFloat(), step.position.wY + Rat.RAT_HEIGHT + rand.nextFloat() * 2);
				ratList.add(rat);
			}
			
			//Generate Fish
			if (wScrollY > WORLD_HEIGHT / 2 && rand.nextFloat() > 0.8f) {
				Fish fish = new Fish(step.position.wX + rand.nextFloat(), step.position.wY + Fish.FISH_HEIGHT + rand.nextFloat() * 2);
				fishList.add(fish);
			}
			
			//Generate Cheese
			if (rand.nextFloat() > 0.7f) {
				Cheese cheese = new Cheese(step.position.wX + rand.nextFloat(), step.position.wY + Cheese.CHEESE_HEIGHT + rand.nextFloat() * 2);
				cheeseList.add(cheese);
			}
			
			wScrollY += maxJumpHeight - 0.5f;
			wScrollY -= rand.nextFloat() * (maxJumpHeight / 5);
		}
		
		//Generate Castle
		castle = new Castle(WORLD_WIDTH / 2, wScrollY);
	}
	
	public void updateWorld(float elapsedTime_s, float accelSensorX) {
		updateCat(elapsedTime_s, accelSensorX);
		updateSteps(elapsedTime_s);
		updateRats(elapsedTime_s);
		updateFishes(elapsedTime_s);
		updateCheeses(elapsedTime_s);
		if (cat.state != Cat.CAT_STATE_CRASH) {
			checkCollisions();
		}
		checkGameOver();
	}
	
	private void updateCat(float elapsedTime_s, float accelSensorX) {
		if (cat.state != Cat.CAT_STATE_CRASH && cat.position.wY <= Cat.CAT_HEIGHT / 2) {
			cat.touchStep();
		}
		if (cat.state != Cat.CAT_STATE_CRASH) {
			cat.velocity.wX = -accelSensorX / 10 * Cat.CAT_MOVE_VELOCITY;
		}
		if (cat.position.wX < 0) {
			cat.position.wX = 0;
		}
		if (cat.position.wX > WORLD_WIDTH) {
			cat.position.wX = WORLD_WIDTH;
		}
		cat.update(elapsedTime_s);
		highestHeight = Math.max(cat.position.wY, highestHeight);
	}
	
	private void updateSteps(float elapsedTime_s) {
		for (int i = 0; i < stepList.size(); i++) {
			Step step = stepList.get(i);
			step.update(elapsedTime_s);
			if (step.state == Step.STEP_STATE_BREAKDOWN && step.stateTime_s > Step.STEP_BREAKDOWN_TIME_S) {
				stepList.remove(step);
			}
		}
	}
	
	private void updateRats(float elapsedTime_s) {
		for (int i = 0; i < ratList.size(); i++) {
			Rat rat = ratList.get(i);
			rat.update(elapsedTime_s);
		}
	}
	
	private void updateFishes(float elapsedTime_s) {
		for (int i = 0; i < fishList.size(); i++) {
			Fish fish = fishList.get(i);
			fish.update(elapsedTime_s);
		}
	}
	
	private void updateCheeses(float elapsedTime_s) {
		for (int i = 0; i < cheeseList.size(); i++) {
			Cheese cheese = cheeseList.get(i);
			cheese.update(elapsedTime_s);
		}
	}
	
	private void checkCollisions() {
		checkStepCollisions();
		checkRatCollisions();
		checkFishCollisions();
		checkItemCollisions();
		checkCastleCollisions();
	}
	
	private void checkStepCollisions() {
		if (cat.velocity.wY > 0) {
			return;
		}
		
		for (int i = 0; i < stepList.size(); i++) {
			Step step = stepList.get(i);
			if (cat.position.wY > step.position.wY) {
				if (CollisionChecker.collisionRectangles(cat.bounds, step.bounds)) {
					cat.touchStep();
					worldListener.jump();
					if (rand.nextFloat() > 1.0f - 0.3f * Settings.level) {
						step.breakdown();
					}
					break;
				}
			}
		}
	}
	
	private void checkRatCollisions() {
		for (int i = 0; i < ratList.size(); i++) {
			Rat rat = ratList.get(i);
			if (CollisionChecker.collisionRectangles(rat.bounds, cat.bounds)) {
				cat.touchRat();
				worldListener.crash();
			}
		}
	}
	
	private void checkFishCollisions() {
		for (int i = 0; i < fishList.size(); i++) {
			Fish fish = fishList.get(i);
			if (CollisionChecker.collisionRectangles(fish.bounds, cat.bounds)) {
				fishList.remove(fish);
				worldListener.get();
				stageScore += Fish.FISH_SCORE;
			}
		}
	}
	
	private void checkItemCollisions() {
		for (int i = 0; i < cheeseList.size(); i++) {
			Cheese cheese = cheeseList.get(i);
			if (CollisionChecker.collisionRectangles(cat.bounds, cheese.bounds)) {
				cheeseList.remove(cheese);
				worldListener.get();
				stageScore += Cheese.CHEESE_SCORE;
			}
		}
		
		if (cat.velocity.wY > 0) {
			return;
		}
		
		for (int i = 0; i < springList.size(); i++) {
			Spring spring = springList.get(i);
			if (cat.position.wY > spring.position.wY) {
				if (CollisionChecker.collisionRectangles(cat.bounds, spring.bounds)) {
					cat.touchSpring();
					worldListener.highjump();
				}
				
			}
		}
	}
	
	private void checkCastleCollisions() {
		if (CollisionChecker.collisionRectangles(cat.bounds, castle.bounds)) {
			worldState = WORLD_STATE_NEXT_STAGE;
		}
	}
	
	private void checkGameOver() {
		if (highestHeight - WorldRenderer.WORLD_FRUSTUM_HEIGHT / 2 > cat.position.wY) {
			worldState = WORLD_STATE_GAME_OVER;
		}
	}	

}
