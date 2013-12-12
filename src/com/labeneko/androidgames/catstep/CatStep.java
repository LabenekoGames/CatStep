package com.labeneko.androidgames.catstep;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.labeneko.androidgames.framework.Screen;
import com.labeneko.androidgames.framework.impl.GLGameActivity;

public class CatStep extends GLGameActivity {
	//Field
	boolean firstGenerate = true;

	//Method
	@Override
	public Screen getStartScreen() {
		return new MainMenuScene(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		if (firstGenerate) {
			Settings.load(getFileIO());
			Assets.load(this);
			firstGenerate = false;
		} else {
			Assets.reload();
		}
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
		if (Settings.soundEnabled) {
			Assets.playMusic();
		}
	}
}