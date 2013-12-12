package com.labeneko.androidgames.catstep;

import javax.microedition.khronos.opengles.GL10;

import com.labeneko.androidgames.framework.gl.Animation;
import com.labeneko.androidgames.framework.gl.Camera2D;
import com.labeneko.androidgames.framework.gl.SpriteDrawer;
import com.labeneko.androidgames.framework.gl.TextureArea;
import com.labeneko.androidgames.framework.impl.GLGraphics;

public class WorldRenderer {
	//Constant
	static final float WORLD_FRUSTUM_WIDTH = 10;
	static final float WORLD_FRUSTUM_HEIGHT = 15;
	
	//Field
	GLGraphics glGraphics;
	WorldSimulator worldSimulator;
	Camera2D worldCam;
	SpriteDrawer batcher;
	
	//Constructor
	public WorldRenderer(GLGraphics glGraphics, WorldSimulator worldSimulator, SpriteDrawer batcher) {
		this.glGraphics = glGraphics;
		this.worldSimulator = worldSimulator;
		this.worldCam = new Camera2D(WORLD_FRUSTUM_WIDTH, WORLD_FRUSTUM_HEIGHT, glGraphics);
		this.batcher = batcher;
	}
	
	//Method
	public void render() {
		if (worldSimulator.cat.position.wY > worldCam.position.wY) {
			worldCam.position.wY = worldSimulator.cat.position.wY;
		}
		worldCam.setViewportAndMatrices();
		renderObjects();
	}
	
	public void renderObjects() {
		batcher.beginBatch(Assets.items);
		renderBackground();
		
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		renderCat();
		renderSteps();
		renderItems();
		renderRats();
		renderFishes();
		renderCastle();
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderBackground() {
		if (Settings.stage % 2 != 0) {
			batcher.drawSprite(worldCam.position.wX, worldCam.position.wY, WORLD_FRUSTUM_WIDTH, WORLD_FRUSTUM_HEIGHT, Assets.room1Bg);
		} else {
			batcher.drawSprite(worldCam.position.wX, worldCam.position.wY, WORLD_FRUSTUM_WIDTH, WORLD_FRUSTUM_HEIGHT, Assets.room2Bg);
		}
	}
	
	private void renderCat() {
		TextureArea targetFrame;
		
		switch (worldSimulator.cat.state) {
		case Cat.CAT_STATE_FALL:
			targetFrame = Assets.catFallAnim.getTargetFrame(worldSimulator.cat.stateTime_s, Animation.ANIMATION_LOOPING);
			break;
			
		case Cat.CAT_STATE_JUMP:
			targetFrame = Assets.catJumpAnim.getTargetFrame(worldSimulator.cat.stateTime_s, Animation.ANIMATION_LOOPING);
			break;
			
		case Cat.CAT_STATE_CRASH:
			//No Animation

		default:
			targetFrame = Assets.catCrash;
			break;
		}
		
		float side = worldSimulator.cat.velocity.wX < 0 ? -1 : 1;
		batcher.drawSprite(worldSimulator.cat.position.wX, worldSimulator.cat.position.wY, side * Cat.CAT_WIDTH, Cat.CAT_HEIGHT, targetFrame);
	}
	
	private void renderSteps() {
		for (int i = 0; i < worldSimulator.stepList.size(); i++) {
			Step step = worldSimulator.stepList.get(i);
			TextureArea targetFrame = Assets.step;
			if (step.state == Step.STEP_STATE_BREAKDOWN) {
				targetFrame = Assets.breakingStepAnim.getTargetFrame(step.stateTime_s, Animation.ANIMATION_NONLOOPING);
			}
			batcher.drawSprite(step.position.wX, step.position.wY, Step.STEP_WIDTH, Step.STEP_HEIGHT, targetFrame);
		}
	}
	
	private void renderItems() {
		for (int i = 0; i < worldSimulator.springList.size(); i++) {
			Spring spring = worldSimulator.springList.get(i);
			batcher.drawSprite(spring.position.wX, spring.position.wY, Spring.SPRING_WIDTH, Spring.SPRING_HEIGHT, Assets.spring);
		}
		for (int i = 0; i < worldSimulator.cheeseList.size(); i++) {
			Cheese cheese = worldSimulator.cheeseList.get(i);
			TextureArea keyFrame = Assets.cheeseAnim.getTargetFrame(cheese.stateTime_s, Animation.ANIMATION_LOOPING);
			batcher.drawSprite(cheese.position.wX, cheese.position.wY, Cheese.CHEESE_WIDTH, Cheese.CHEESE_HEIGHT, keyFrame);
		}
	}
	
	private void renderRats() {
		for (int i = 0; i < worldSimulator.ratList.size(); i++) {
			Rat rat = worldSimulator.ratList.get(i);
			TextureArea targetFrame = Assets.ratAnim.getTargetFrame(rat.stateTime_s, Animation.ANIMATION_LOOPING);
			float side = rat.velocity.wX < 0 ? -1 : 1;
			batcher.drawSprite(rat.position.wX, rat.position.wY, side * Rat.RAT_WIDTH, Rat.RAT_HEIGHT, targetFrame);
		}
	}
	
	private void renderFishes() {
		for (int i = 0; i < worldSimulator.fishList.size(); i++) {
			Fish fish = worldSimulator.fishList.get(i);
			TextureArea targetFrame = Assets.fishAnim.getTargetFrame(fish.stateTime_s, Animation.ANIMATION_LOOPING);
			float side = fish.velocity.wX < 0 ? -1 : 1;
			batcher.drawSprite(fish.position.wX, fish.position.wY, side * Fish.FISH_WIDTH, Fish.FISH_HEIGHT, targetFrame);
		}
	}
	
	private void renderCastle() {
		Castle castle = worldSimulator.castle;
		batcher.drawSprite(castle.position.wX, castle.position.wY, Castle.CASTLE_WIDTH, Castle.CASTLE_HEIGHT, Assets.castle);
	}
}
