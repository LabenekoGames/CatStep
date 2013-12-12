package com.labeneko.androidgames.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.res.AssetManager;

import com.labeneko.androidgames.framework.FileIO;

public class AndroidFileIO implements FileIO {
	AssetManager assets;
	String extStoragePath;

	public AndroidFileIO(Activity activity) {
		this.assets = activity.getAssets();
		extStoragePath = activity.getExternalFilesDir(null) + File.separator;
	}
	
	@Override
	public InputStream readAsset(String fileName) throws IOException {
		return assets.open(fileName);
	}
	
	@Override
	public InputStream readFile(String fileName) throws IOException {
		return new FileInputStream(extStoragePath + fileName);
	}
	
	@Override
	public OutputStream writeFile(String fileName) throws IOException {
		return new FileOutputStream(extStoragePath + fileName);
	}

}
