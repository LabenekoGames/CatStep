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

public class ScoreScene extends GLScreen {
	//Constant
	int NUM_MAX_SPRITE = 100;
	
	//Field
	Camera2D guiCam;
	SpriteDrawer batcher;
	Rectangle backBounds;
	Vector2d touchPoint;
	String[] top5Scores;
	float xOffset = 0;

	//Constructor
	public ScoreScene(Game game) {
		super(game);
		guiCam = new Camera2D(320, 480, glGraphics);
		batcher = new SpriteDrawer(glGraphics, NUM_MAX_SPRITE);
		backBounds = new Rectangle(0, 0, 64, 64);
		touchPoint = new Vector2d();
		top5Scores = new String[5];

		for (int i = 0; i < top5Scores.length; i++) {
			top5Scores[i] = (i + 1 ) + ". " + Settings.top5scores[i];
			xOffset = Math.max(top5Scores[i].length() * Assets.textWriter.charWidth, xOffset);
		}

		xOffset = 320 / 2 - xOffset / 2 + Assets.textWriter.charWidth / 2;
	}

	@Override
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void pause() {
		Settings.save(game.getFileIO());
		if (Settings.soundEnabled) {
			Assets.pauseMusic();
		}
	}

	@Override
	public void present(float deltaTime_sec) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		batcher.beginBatch(Assets.items);
		batcher.drawSprite(160, 240, 320, 480, Assets.room1Bg);
		batcher.drawSprite(160, 360, 160, 48, Assets.ScoreRegion);

		float wScoreY = 300;
		for (int i = 0; i < top5Scores.length; i++) {
			Assets.textWriter.drawText(batcher, top5Scores[i], xOffset, wScoreY);
			wScoreY -= Assets.textWriter.charHeight;
		}

		batcher.drawSprite(32, 32, 64, 64, Assets.arrow);
		batcher.endBatch();

		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void resume() {
		//if (Settings.soundEnabled) {
		//	Assets.playMusic();
		//}
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

				if (CollisionChecker.pointInRectangle(backBounds, touchPoint)) {
					Assets.playSound(Assets.clickSound);
					game.setScreen(new MainMenuScene(game));
					return;
				}
			}
		}
	}
}
