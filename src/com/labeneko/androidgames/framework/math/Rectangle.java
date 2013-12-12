package com.labeneko.androidgames.framework.math;

public class Rectangle {
	//Field
	public final Vector2d lowerLeft;
	public float width, height;
	
	//Constructor
	public Rectangle(float wLowerLeftX, float wLowerLeftY, float width, float height) {
		this.lowerLeft = new Vector2d(wLowerLeftX, wLowerLeftY);
		this.width = width;
		this.height = height;
	}

}
