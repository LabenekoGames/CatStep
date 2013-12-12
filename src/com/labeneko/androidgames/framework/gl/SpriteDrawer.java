package com.labeneko.androidgames.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.labeneko.androidgames.framework.impl.GLGraphics;

public class SpriteDrawer {
	//Field
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numSprites;
	
	//Constructor
	public SpriteDrawer(GLGraphics glGraphics, int maxSprites) {
		this.verticesBuffer = new float[maxSprites * 4 * 4];
		this.vertices = new Vertices(glGraphics, maxSprites * 4, maxSprites * 6, false, true);
		
		this.bufferIndex = 0;
		this.numSprites = 0;
		
		short[] indices = new short[maxSprites * 6];
		short j = 0;
		for (int i = 0; i < indices.length; i += 6, j +=4) {
			indices[i + 0] = (short) (j + 0);
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = (short) (j + 0);
		}
		
		vertices.setIndices(indices, 0, indices.length);
	}
	
	//Method
	public void beginBatch(Texture texture) {
		texture.bind();
		numSprites = 0;
		bufferIndex = 0;
	}
	
	public void endBatch() {
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
		vertices.unbind();
	}
	
	public void drawSprite(float wCenterX, float wCenterY, float width, float height, TextureArea area) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		
		float wLeftX = wCenterX - halfWidth;
		float wLowerY = wCenterY - halfHeight;
		
		float wRightX = wCenterX + halfWidth;
		float wUpperY = wCenterY + halfHeight;
		
		verticesBuffer[bufferIndex] = wLeftX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = wLowerY;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tLeftX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tLowerY;
		bufferIndex++;
		
		verticesBuffer[bufferIndex] = wRightX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = wLowerY;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tRightX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tLowerY;
		bufferIndex++;
		
		verticesBuffer[bufferIndex] = wRightX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = wUpperY;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tRightX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tUpperY;
		bufferIndex++;
		
		verticesBuffer[bufferIndex] = wLeftX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = wUpperY;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tLeftX;
		bufferIndex++;
		verticesBuffer[bufferIndex] = area.tUpperY;
		bufferIndex++;
		
		numSprites++;
	}
}
