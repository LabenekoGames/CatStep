package com.labeneko.androidgames.catstep;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.labeneko.androidgames.framework.Game;
import com.labeneko.androidgames.framework.Input.TouchEvent;
import com.labeneko.androidgames.framework.gl.Camera2D;
import com.labeneko.androidgames.framework.gl.SpriteDrawer;
import com.labeneko.androidgames.framework.impl.GLScreen;
import com.labeneko.androidgames.framework.math.CollisionChecker;
import com.labeneko.androidgames.framework.math.Rectangle;
import com.labeneko.androidgames.framework.math.Vector2d;

public class MainMenuScene extends GLScreen {
	//Constant
	int NUM_MAX_SPRITE = 100;
	
	//Field
	Camera2D guiCam;
	SpriteDrawer batcher;
	Rectangle soundBounds;
	Rectangle playBounds;
	Rectangle scoreBounds;
	Vector2d touchPoint;
	
	//Constructor
	public MainMenuScene(Game game) {
		super(game);
		guiCam = new Camera2D(320, 480, glGraphics);
		batcher = new SpriteDrawer(glGraphics, NUM_MAX_SPRITE);
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(80, 240 - 48, 160, 48);
		scoreBounds = new Rectangle(80, 240 - 48 * 2, 160, 48);
		touchPoint = new Vector2d();
	}

	@Override
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void pause() {
		Settings.save(game.getFileIO());
		if (Settings.soundEnabled) {
			Assets.openingMusic.pause();
		}
	}

	@Override
	public void present(float elapsedTime_s) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		batcher.drawSprite(160, 240, 320, 480, Assets.doorBg);
		batcher.drawSprite(160, 480 - 48 - 64, 274 * 0.8f, 128 * 0.8f, Assets.titlelogo);
		batcher.drawSprite(160, 240 - 48, 160, 96, Assets.mainMenu);
		batcher.drawSprite(32, 32, 64, 64, Settings.soundEnabled ? Assets.soundOn : Assets.soundOff);
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void resume() {

	}

	@Override
	public void update(float elapsedTime_s) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				touchPoint.set(event.pX, event.pY);
				guiCam.touchToWorld(touchPoint);
				
				if (CollisionChecker.pointInRectangle(playBounds, touchPoint)) {
					Assets.playSound(Assets.openDoorSound);
					game.setScreen(new GameScene(game));
					return;
				}
				if (CollisionChecker.pointInRectangle(scoreBounds, touchPoint)) {
					Assets.playSound(Assets.openDoorSound);
					game.setScreen(new ScoreScene(game));
					return;
				}
				if (CollisionChecker.pointInRectangle(soundBounds, touchPoint)) {
					Settings.soundEnabled = !Settings.soundEnabled;
					if (Settings.soundEnabled) {
						Assets.playSound(Assets.clickSound);
						Assets.playMusic();
					} else {
						Assets.pauseMusic();
					}
				}
				
			}
		}
	}
}
