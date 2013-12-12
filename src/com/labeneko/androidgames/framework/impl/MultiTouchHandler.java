package com.labeneko.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import com.labeneko.androidgames.framework.Input.TouchEvent;
import com.labeneko.androidgames.framework.Pool;
import com.labeneko.androidgames.framework.Pool.PoolObjectFactory;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MultiTouchHandler implements TouchHandler {
	//Field
	boolean[] isTouched = new boolean[20];
	int[] touchX = new int[20];
	int[] touchY = new int[20];
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	//Constructor
	public MultiTouchHandler(View view, float scaleX, float scaleY) {
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
					int action = event.getAction() & MotionEvent.ACTION_MASK;
					int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
					int pointerId = event.getPointerId(pointerIndex);
					
					TouchEvent touchEvent;
					
					switch(action) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_POINTER_DOWN:
						touchEvent = touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_DOWN;
						touchEvent.pointer = pointerId;
						touchEvent.pX = touchX[pointerId] = (int) (event.getX(pointerIndex) * MultiTouchHandler.this.scaleX);
						touchEvent.pY = touchY[pointerId] = (int) (event.getY(pointerIndex) * MultiTouchHandler.this.scaleY);
						isTouched[pointerId] = true;
						touchEventsBuffer.add(touchEvent);
						break;
						
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_POINTER_UP:
					case MotionEvent.ACTION_CANCEL:
						touchEvent = touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_UP;
						touchEvent.pointer = pointerId;
						touchEvent.pX = touchX[pointerId] = (int) (event.getX(pointerIndex) * MultiTouchHandler.this.scaleX);
						touchEvent.pY = touchY[pointerId] = (int) (event.getY(pointerIndex) * MultiTouchHandler.this.scaleY);
						isTouched[pointerId] = false;
						touchEventsBuffer.add(touchEvent);
						break;
						
					case MotionEvent.ACTION_MOVE:
						int pointerCount = event.getPointerCount();
						for (int i = 0; i < pointerCount; i++) {
							pointerIndex = i;
							pointerId = event.getPointerId(pointerIndex);
							
							touchEvent = touchEventPool.newObject();
							touchEvent.type = TouchEvent.TOUCH_DRAGGED;
							touchEvent.pointer = pointerId;
							touchEvent.pX = touchX[pointerId] = (int) (event.getX(pointerIndex) * MultiTouchHandler.this.scaleX);
							touchEvent.pY = touchY[pointerId] = (int) (event.getY(pointerIndex) * MultiTouchHandler.this.scaleY);
							touchEventsBuffer.add(touchEvent);
						}
						break;
					}
					
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
			if(pointer < 0 || pointer > 20)
				return false;
			else
				return isTouchDown(pointer);
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			if(pointer < 0 || pointer > 20)
				return 0;
			else
				return touchX[pointer];
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			if(pointer < 0 || pointer > 20)
				return 0;
			else
				return touchY[pointer];
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
