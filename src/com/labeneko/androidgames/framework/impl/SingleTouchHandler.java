package com.labeneko.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import com.labeneko.androidgames.framework.Input.TouchEvent;
import com.labeneko.androidgames.framework.Pool;
import com.labeneko.androidgames.framework.Pool.PoolObjectFactory;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SingleTouchHandler implements TouchHandler {
	//Field
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	//Constructor
	public SingleTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				synchronized (this) {
					TouchEvent touchEvent = touchEventPool.newObject();
					
					switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						touchEvent.type = TouchEvent.TOUCH_DOWN;
						isTouched = true;
						break;
					case MotionEvent.ACTION_MOVE:
						touchEvent.type = TouchEvent.TOUCH_DRAGGED;
						isTouched = true;
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						touchEvent.type = TouchEvent.TOUCH_UP;
						isTouched = false;
						break;
					}
					
					touchEvent.pX = touchX = (int) (event.getX() * SingleTouchHandler.this.scaleX);
					touchEvent.pY = touchY = (int) (event.getY() * SingleTouchHandler.this.scaleY);
					touchEventsBuffer.add(touchEvent);
					
					return true;
				}
			}
		});
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	//Method
	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if(pointer == 0)
				return isTouched;
			else
				return false;
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			return touchX;
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			return touchY;
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = touchEvents.size();
			for(int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
}
