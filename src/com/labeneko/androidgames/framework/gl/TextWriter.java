package com.labeneko.androidgames.framework.gl;

public class TextWriter {
	//Field
	public final Texture texture;
	public final int charWidth;
	public final int charHeight;
	public final TextureArea[] chars = new TextureArea[100];
	
	//Constructor
	public TextWriter(Texture texture, int pUpperLeftX, int pUpperLeftY, int charPerRow, int charWidth, int charHeight) {
		this.texture = texture;
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		int x = pUpperLeftX;
		int y = pUpperLeftY;
		
		for (int i = 0; i < chars.length; i++) {
			chars[i] = new TextureArea(texture, x, y, charWidth, charHeight);
			x += charWidth;
			if (x == pUpperLeftX + charPerRow * charWidth) {
				x = pUpperLeftX;
				y += charHeight;
			}
		}
	}
	
	//Method
	public void drawText(SpriteDrawer batcher, String text, float firstChar_wCenterX, float firstChar_wCenterY) {
		for (int i = 0; i < text.length(); i++) {
			int c = text.charAt(i) - ' ';			
			if (c < 0 || c > chars.length - 1) {
				continue;
			}
			batcher.drawSprite(firstChar_wCenterX, firstChar_wCenterY, charWidth, charHeight, chars[c]);
			firstChar_wCenterX += charWidth;
		}
	}
}
