package com.labeneko.androidgames.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.labeneko.androidgames.catstep.R;
import com.labeneko.androidgames.framework.Audio;
import com.labeneko.androidgames.framework.FileIO;
import com.labeneko.androidgames.framework.Game;
import com.labeneko.androidgames.framework.Input;
import com.labeneko.androidgames.framework.Screen;

public abstract class GLGameActivity extends Activity implements Game, Renderer {
	//Field
	enum GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	
	GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime_nsec = System.nanoTime();
	WakeLock wakeLock;
	
	//Method
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);
		
		float scaleX = 1;
		float scaleY = 1;
		
		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, scaleX, scaleY);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}
	
	@Override
	public void onPause() {
		synchronized (stateChanged) {
			if(isFinishing())
				state = GLGameState.Finished;
			else
				state = GLGameState.Paused;
			
			while(true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {
					Log.e("stateChanged.wait()", e.getMessage());
				}
			}
		}
		
		glView.onPause();
		super.onPause();
		wakeLock.release();
	}
	
	//Renderer
	@Override
	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;
		synchronized (stateChanged) {
			state = this.state;
		}
		
		if(state == GLGameState.Running) {
			float deltaTime_sec = (System.nanoTime()-startTime_nsec) * 1.0e-9f;
			startTime_nsec = System.nanoTime();
			
			screen.update(deltaTime_sec);
			screen.present(deltaTime_sec);
		}
		
		if(state == GLGameState.Paused) {
			screen.pause();
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
		
		if(state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}

	}
	
	//Renderer
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
	}
	
	//Renderer
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glGraphics.setGL(gl);
		
		synchronized (stateChanged) {
			if(state == GLGameState.Initialized)
				screen = getStartScreen();
			state = GLGameState.Running;
			screen.resume();
			startTime_nsec = System.nanoTime();
		}
	}
	
	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if(screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		
		this.screen.pause();
		this.screen.dispose();
		
		screen.resume();
		screen.update(0);
		
		this.screen = screen;
	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}
}

