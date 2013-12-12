package com.labeneko.androidgames.framework.impl;

import java.util.List;

import com.labeneko.androidgames.framework.Input.TouchEvent;

public interface TouchHandler {
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<TouchEvent> getTouchEvents();

}
