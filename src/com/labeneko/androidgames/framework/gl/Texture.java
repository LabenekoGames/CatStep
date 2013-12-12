package com.labeneko.androidgames.framework.gl;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import com.labeneko.androidgames.framework.FileIO;
import com.labeneko.androidgames.framework.impl.GLGameActivity;
import com.labeneko.androidgames.framework.impl.GLGraphics;

public class Texture {
	//Field
	GLGraphics glGraphics;
	FileIO fileIO;
	String fileName;
	int textureId;
	int minFilter;
	int magFilter;
	
	float width = 0;
	float height = 0;
	
	//Constructor
	//public Texture(GLGame glGame, String fileName) {
	//	this.glGraphics = glGame.getGLGraphics();
	//	this.fileIO = glGame.getFileIO();
	//	this.fileName = fileName;
	//	load();
	//}
	
	public Texture(GLGameActivity glGame, String fileName, float width, float height) {
		this.glGraphics = glGame.getGLGraphics();
		this.fileIO = glGame.getFileIO();
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		load();
	}
	
	//Method
	private void load() {
		GL10 gl = glGraphics.getGL();
		int[] textureIds = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
		
		InputStream in = null;
		try {
			in = fileIO.readAsset(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load texure '" + fileName + "'", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("Texture", e.getMessage());
				}
			}
		}
	}
	
	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}
	
	private void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		
		GL10 gl = glGraphics.getGL();
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, minFilter);
	}
	
	public void bind() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}
	
	public void dispose() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		int[] textureIds = { textureId };
		gl.glDeleteTextures(1, textureIds, 0);
	}

}
