package com.labeneko.androidgames.framework.impl;

import com.labeneko.androidgames.framework.Game;
import com.labeneko.androidgames.framework.Screen;

public abstract class GLScreen extends Screen {
	//Field
	protected final GLGraphics glGraphics;
	protected final GLGameActivity glGame;
	
	//Constructor
	public GLScreen(Game game) {
		super(game);
		glGame = (GLGameActivity)game;
		glGraphics = ((GLGameActivity)game).getGLGraphics();
	}
	
}
