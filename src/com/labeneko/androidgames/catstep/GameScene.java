package com.labeneko.androidgames.catstep;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.labeneko.androidgames.catstep.WorldSimulator.WorldListener;
import com.labeneko.androidgames.framework.Game;
import com.labeneko.androidgames.framework.Input.TouchEvent;
import com.labeneko.androidgames.framework.gl.Camera2D;
import com.labeneko.androidgames.framework.gl.SpriteDrawer;
import com.labeneko.androidgames.framework.impl.GLScreen;
import com.labeneko.androidgames.framework.math.CollisionChecker;
import com.labeneko.androidgames.framework.math.Rectangle;
import com.labeneko.androidgames.framework.math.Vector2d;

public class GameScene extends GLScreen {
	//Constant
	public static final float GUI_FRUSTUM_WIDTH = 320;
	public static final float GUI_FRUSTUM_HEIGHT = 480;
	public static final int SCENE_STATE_READY = 0;
	public static final int SCENE_STATE_PLAYING = 1;
	public static final int SCENE_STATE_PAUSE = 2;
	public static final int SCENE_STATE_STAGE_END = 3;
	public static final int SCENE_STAGE_OVER = 4;

	//Field
	
	Camera2D guiCam;
	Vector2d touchPoint;
	SpriteDrawer batcher;
	WorldSimulator worldSimulator;
	WorldListener worldListener;
	WorldRenderer worldRenderer;
	Rectangle soundBounds;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	int sceneState;
	int stageScore;
	int lastScore;
	int totalScore;
	String stageString;
	String scoreString;

	//Constructor
	public GameScene(Game game) {
		super(game);
		sceneState = SCENE_STATE_READY;
		guiCam = new Camera2D(GUI_FRUSTUM_WIDTH, GUI_FRUSTUM_HEIGHT, glGraphics);
		touchPoint = new Vector2d();
		batcher = new SpriteDrawer(glGraphics, 1000);
		worldListener = new WorldListener() {

			@Override
			public void jump() {
				Assets.playSound(Assets.jumpSound);
			}

			@Override
			public void crash() {
				Assets.playSound(Assets.crashSound);				
			}

			@Override
			public void highjump() {
				Assets.playSound(Assets.highJumpSound);
			}

			@Override
			public void get() {
				Assets.playSound(Assets.getSound);
			}
		};
		
		worldSimulator = new WorldSimulator(worldListener);
		worldRenderer = new WorldRenderer(glGraphics, worldSimulator, batcher);
		
		soundBounds = new Rectangle(0, 0, 64, 64);
		pauseBounds = new Rectangle(GUI_FRUSTUM_WIDTH - 64, 0, 64, 64);
		resumeBounds = new Rectangle(GUI_FRUSTUM_WIDTH / 2 - 96, GUI_FRUSTUM_HEIGHT / 2, 192, 48);
		quitBounds = new Rectangle(GUI_FRUSTUM_WIDTH / 2 - 96, GUI_FRUSTUM_HEIGHT / 2 - 48, 192, 48);
		stageScore = 0;
		lastScore = 0;
		totalScore = 0;
		stageString = "stage:0";
		scoreString = "score:0";
	}

	@Override
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void pause() {
		if (sceneState == SCENE_STATE_PLAYING) {
			sceneState = SCENE_STATE_PAUSE;
		}
		
		Settings.save(game.getFileIO());
		if (Settings.soundEnabled) {
			Assets.pauseMusic();
		}
	}

	@Override
	public void present(float elapsedTime_s) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		worldRenderer.render();

		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.items);

		switch (sceneState) {
		case SCENE_STATE_READY:
			presentReady();
			break;
		case SCENE_STATE_PLAYING:
			presentPlaying();
			break;
		case SCENE_STATE_PAUSE:
			presentPaused();
			break;
		case SCENE_STATE_STAGE_END:
			presentStageEnd();
			break;
		case SCENE_STAGE_OVER:
			presentGameOver();
			break;
		}
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}

	private void presentReady() {
		batcher.drawSprite(GUI_FRUSTUM_WIDTH / 2, GUI_FRUSTUM_HEIGHT / 2, 192, 32, Assets.ready);
	}

	private void presentPlaying() {
		batcher.drawSprite(32, 32, 64, 64, Settings.soundEnabled ? Assets.soundOn : Assets.soundOff);
		batcher.drawSprite(GUI_FRUSTUM_WIDTH - 32, 32, 64, 64, Assets.pause);
		Assets.textWriter.drawText(batcher, stageString, 16, GUI_FRUSTUM_HEIGHT - 20);
		Assets.textWriter.drawText(batcher, scoreString, 16, GUI_FRUSTUM_HEIGHT - 40);
	}

	private void presentPaused() {
		batcher.drawSprite(GUI_FRUSTUM_WIDTH / 2, GUI_FRUSTUM_HEIGHT / 2, 192, 96, Assets.pauseMenu);
		Assets.textWriter.drawText(batcher, stageString, 16, GUI_FRUSTUM_HEIGHT - 20);
		Assets.textWriter.drawText(batcher, scoreString, 16, GUI_FRUSTUM_HEIGHT - 40);
	}

	private void presentStageEnd() {
		String firstText = "Congratulations!";
		String secondText = "Go next stage!";
		float topTextWidth = Assets.textWriter.charWidth * firstText.length();
		float bottomTextWidth = Assets.textWriter.charWidth * secondText.length();
		Assets.textWriter.drawText(batcher, firstText, GUI_FRUSTUM_WIDTH / 2 - topTextWidth / 2, GUI_FRUSTUM_HEIGHT - 40);
		Assets.textWriter.drawText(batcher, secondText, GUI_FRUSTUM_WIDTH / 2 - bottomTextWidth / 2, GUI_FRUSTUM_HEIGHT - 80);
	}

	private void presentGameOver() {
		batcher.drawSprite(GUI_FRUSTUM_WIDTH / 2, GUI_FRUSTUM_HEIGHT / 2, 128, 96, Assets.gameOver);
		Assets.textWriter.drawText(batcher, stageString, 16, GUI_FRUSTUM_HEIGHT - 20);
		Assets.textWriter.drawText(batcher, scoreString, 16, GUI_FRUSTUM_HEIGHT - 40);
	}

	@Override
	public void resume() {
		//if (Settings.soundEnabled) {
		//	Assets.playMusic();
		//}
	}

	@Override
	public void update(float elapsedTime_s) {
		if (elapsedTime_s > 0.1f) {
			elapsedTime_s = 0.1f;
		}

		switch (sceneState) {
		case SCENE_STATE_READY:
			updateReady();
			break;
		case SCENE_STATE_PLAYING:
			updateRunning(elapsedTime_s);
			break;
		case SCENE_STATE_PAUSE:
			updatePaused();
			break;
		case SCENE_STATE_STAGE_END:
			updateStageEnd();
			break;
		case SCENE_STAGE_OVER:
			updateGameOver();
			break;
		}
	}

	private void updateReady() {
		if (game.getInput().getTouchEvents().size() > 0) {
			stageString = "stage:" + Settings.stage;
			sceneState = SCENE_STATE_PLAYING;
		}
	}

	private void updateRunning(float elapsedTime_s) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type != TouchEvent.TOUCH_UP) {
				continue;
			}

			touchPoint.set(event.pX, event.pY);
			guiCam.touchToWorld(touchPoint);
			
			if (CollisionChecker.pointInRectangle(soundBounds, touchPoint)) {
				Settings.soundEnabled = !Settings.soundEnabled;
				if (Settings.soundEnabled) {
					Assets.playSound(Assets.clickSound);
					Assets.playMusic();
				} else {
					Assets.pauseMusic();
				}
			}
			if (CollisionChecker.pointInRectangle(pauseBounds, touchPoint)) {
				Assets.playSound(Assets.clickSound);
				sceneState = SCENE_STATE_PAUSE;
				return;
			}
		}

		worldSimulator.updateWorld(elapsedTime_s, game.getInput().getAccelX());
		
		if (stageScore != worldSimulator.stageScore) {
			stageScore = worldSimulator.stageScore;
			totalScore = lastScore + stageScore;
			scoreString = "score:" + totalScore;
		}
		
		if (worldSimulator.worldState == WorldSimulator.WORLD_STATE_NEXT_STAGE) {
			sceneState = SCENE_STATE_STAGE_END;
			lastScore += stageScore;
		}
		
		if (worldSimulator.worldState == WorldSimulator.WORLD_STATE_GAME_OVER) {
			sceneState = SCENE_STAGE_OVER;
			if (totalScore > Settings.top5scores[4]) {
				scoreString = "new highscore:" + totalScore;
			} else {
				scoreString = "score:" + totalScore;
			}
			Settings.addScore(totalScore);
			Settings.save(game.getFileIO());
			
			stageScore = 0;
			lastScore = 0;
		}
	}

	private void updatePaused() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type != TouchEvent.TOUCH_UP) {
				continue;
			}

			touchPoint.set(event.pX, event.pY);
			guiCam.touchToWorld(touchPoint);

			if (CollisionChecker.pointInRectangle(resumeBounds, touchPoint)) {
				Assets.playSound(Assets.clickSound);
				sceneState = SCENE_STATE_PLAYING;
				return;
			}
			if (CollisionChecker.pointInRectangle(quitBounds, touchPoint)) {
				Settings.stage = 1;
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScene(game));
				return;
			}
		}
	}

	private void updateStageEnd() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type != TouchEvent.TOUCH_UP) {
				continue;
			}
			Settings.stage += 1;
			worldSimulator = new WorldSimulator(worldListener);
			worldRenderer = new WorldRenderer(glGraphics, worldSimulator, batcher);
			sceneState = SCENE_STATE_READY;
		}
	}

	private void updateGameOver() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type != TouchEvent.TOUCH_UP) {
				continue;
			}
			Settings.stage = 1;
			game.setScreen(new MainMenuScene(game));
		}
	}
}
