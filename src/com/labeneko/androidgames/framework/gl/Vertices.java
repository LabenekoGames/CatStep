package com.labeneko.androidgames.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.labeneko.androidgames.framework.impl.GLGraphics;

public class Vertices {
	//Field
	private GLGraphics glGraphics;
	private boolean hasColor;
	private boolean hasTexCoords;
	private int vertexSize;
	//private FloatBuffer verticesBuffer;
	
	private IntBuffer verticesIntBuffer;
	private int[] tmpBuffer;
	
	private ShortBuffer indicesBuffer;
	
	//Constructor
	public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords) {
		this.glGraphics = glGraphics;
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (2+ (hasColor? 4:0) + (hasTexCoords? 2:0)) * 4;
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		
		//verticesBuffer = buffer.asFloatBuffer();
		
		verticesIntBuffer = buffer.asIntBuffer();
		this.tmpBuffer = new int[maxVertices * vertexSize / 4];
		
		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			
			indicesBuffer = buffer.asShortBuffer();
		} else {
			indicesBuffer = null;
		}
	}
	
	//Method
	public void setVertices(float[] vertices, int offset, int length) {
		this.verticesIntBuffer.clear();
		int len = offset + length;
		
		for (int i = offset, j = 0; i < len; i++, j++) {
			tmpBuffer[j] = Float.floatToIntBits(vertices[i]);
		}
		
		this.verticesIntBuffer.put(tmpBuffer, 0, length);
		this.verticesIntBuffer.flip();
		
		//this.verticesBuffer.clear();
		//this.verticesBuffer.put(vertices, offset, length);
		//this.verticesBuffer.flip();
	}
	
	public void setIndices(short[] indices, int offset, int length) {
		this.indicesBuffer.clear();
		this.indicesBuffer.put(indices, offset, length);
		this.indicesBuffer.flip();
	}
	
	public void bind() {
		GL10 gl = glGraphics.getGL();
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//verticesBuffer.position(0);
		//gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, verticesBuffer);
		verticesIntBuffer.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, verticesIntBuffer);
		
		if (hasColor) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			//verticesBuffer.position(2);
			//gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, verticesBuffer);
			verticesIntBuffer.position(2);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, verticesIntBuffer);
		}
		
		if (hasTexCoords) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			//verticesBuffer.position(hasColor ? 6 : 2);
			//gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, verticesBuffer);
			verticesIntBuffer.position(hasColor ? 6 : 2);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, verticesIntBuffer);
		}
	}
	
	public void draw(int primitiveType, int offset, int numVertices) {
		GL10 gl = glGraphics.getGL();
		
		if (indicesBuffer != null) {
			indicesBuffer.position(offset);
			gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		} else {
			gl.glDrawArrays(primitiveType, offset, numVertices);
		}
	}
	
	public void unbind() {
		GL10 gl = glGraphics.getGL();
		
		if (hasColor) {
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		
		if (hasTexCoords) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}
	
}
