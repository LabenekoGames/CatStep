package com.labeneko.androidgames.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.labeneko.androidgames.framework.impl.GLGraphics;
import com.labeneko.androidgames.framework.math.Vector2d;

public class Camera2D {
	//Field
	public final Vector2d position;
	public float zoom;
	public final float frustumWidth;
	public final float frustumHeight;
	final GLGraphics glGraphics;
	
	//Constructor
	public Camera2D(float frustumWidth, float frustumHeight, GLGraphics glGraphics) {
		this.frustumWidth = frustumWidth;
		this.frustumHeight = frustumHeight;
		this.glGraphics = glGraphics;
		this.position = new Vector2d(frustumWidth / 2, frustumHeight / 2);
		this.zoom = 1.0f;
	}
	
	//Method
	public void setViewportAndMatrices() {
		GL10 gl = glGraphics.getGL();
		
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.wX - frustumWidth * zoom / 2, position.wX + frustumWidth * zoom / 2,
				position.wY - frustumHeight * zoom / 2, position.wY + frustumHeight * zoom / 2, 1, -1);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void touchToWorld(Vector2d touch) {
		touch.wX = (touch.wX / glGraphics.getWidth()) * frustumWidth * zoom;
		touch.wY = (1 - touch.wY / glGraphics.getHeight()) * frustumHeight * zoom;
	}
	

}
