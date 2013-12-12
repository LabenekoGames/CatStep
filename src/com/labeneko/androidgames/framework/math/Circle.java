package com.labeneko.androidgames.framework.math;

public class Circle {
	//Field
	public final Vector2d center;
	public float radius;
	
	//Constructor
	public Circle(float x, float y, float radius) {
		this.center = new Vector2d(x, y);
		this.radius = radius;
	}

}
