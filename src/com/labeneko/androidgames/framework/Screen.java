package com.labeneko.androidgames.framework;

public abstract class Screen {
	//Field
	protected final Game game;
	
	//Constructor
	public Screen(Game game) {
		this.game = game;
	}
	
	//Method
	public abstract void update(float elapsedTime_s);
	
	public abstract void present(float elapsedTime_s);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public abstract void dispose();
	
}
