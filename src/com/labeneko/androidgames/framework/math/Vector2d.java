package com.labeneko.androidgames.framework.math;

public class Vector2d {
	//Field
	public static float TO_RADIANS = (1 / 180.0f) * (float) Math.PI;
	public static float TO_DEGREES = 180.0f / ((float) Math.PI);
	public float wX, wY;
	
	//Constructor
	public Vector2d() {
	}
	
	public Vector2d(float wX, float wY) {
		this.wX = wX;
		this.wY = wY;
	}
	
	public Vector2d(Vector2d other) {
		this.wX = other.wX;
		this.wY = other.wY;
	}
	
	//Method
	public Vector2d cpy() {
		return new Vector2d(wX, wY);
	}
	
	public Vector2d set(float wX, float wY) {
		this.wX = wX;
		this.wY = wY;
		return this;
	}
	
	public Vector2d set(Vector2d other) {
		this.wX = other.wX;
		this.wY = other.wY;
		return this;
	}
	
	public Vector2d add(float wX, float wY) {
		this.wX += wX;
		this.wY += wY;
		return this;
	}
	
	public Vector2d add(Vector2d other) {
		this.wX += other.wX;
		this.wY += other.wY;
		return this;
	}
	
	public Vector2d sub(float wX, float wY) {
		this.wX -= wX;
		this.wY -= wY;
		return this;
	}
	
	public Vector2d sub(Vector2d other) {
		this.wX -= other.wX;
		this.wY -= other.wY;
		return this;
	}
	
	public Vector2d mul(float scalar) {
		this.wX *= scalar;
		this.wY *= scalar;
		return this;
	}
	
	public float len() {
		return (float) Math.sqrt(wX * wX + wY * wY);
	}
	
	public Vector2d nor() {
		float len = len();
		if (len != 0) {
			this.wX /= len;
			this.wY /= len;
		}
		return this;
	}
	
	public float angle() {
		float angle = (float) Math.atan2(wY, wX) * TO_DEGREES;
		return angle;
	}
	
	public Vector2d rotate(float angle) {
		float rad = angle * TO_RADIANS;
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		
		float newX = this.wX * cos - this.wY * sin;
		float newY = this.wX * sin + this.wY * cos;
		
		this.wX = newX;
		this.wY = newY;
		
		return this;
	}
	
	public float dist(Vector2d other) {
		float distX = this.wX - other.wX;
		float distY = this.wY - other.wY;
		return (float) Math.sqrt(distX * distX + distY * distY);
	}
	
	public float dist(float wX, float wY) {
		float distX = this.wX - wX;
		float distY = this.wY - wY;
		return (float) Math.sqrt(distX * distX + distY * distY);
	}
	
	public float distSquared(Vector2d other) {
		float distX = this.wX - other.wX;
		float distY = this.wY - other.wY;
		return distX * distX + distY * distY;
	}
	
	public float distSquared(float wX, float wY) {
		float distX = this.wX - wX;
		float distY = this.wY - wY;
		return distX * distX + distY * distY;
	}

}
