package com.labeneko.androidgames.framework.gl;

public class TextureArea {
	//Field
	public final float tLeftX, tUpperY;
	public final float tRightX, tLowerY;
	public final Texture texture;
	
	//Constructor
	public TextureArea(Texture texture, float pUpperLeftX, float pUpperLeftY, float width, float height) {
		this.tLeftX = pUpperLeftX / texture.width;
		this.tUpperY = pUpperLeftY / texture.height;
		
		this.tRightX = this.tLeftX + width / texture.width;
		this.tLowerY = this.tUpperY + height / texture.height;
		
		this.texture = texture;
	}

}
