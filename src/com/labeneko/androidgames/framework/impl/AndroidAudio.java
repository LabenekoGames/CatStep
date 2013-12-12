package com.labeneko.androidgames.framework.impl;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.labeneko.androidgames.framework.Audio;
import com.labeneko.androidgames.framework.Music;
import com.labeneko.androidgames.framework.Sound;

public class AndroidAudio implements Audio {
	//Field
	AssetManager assets;
	SoundPool soundPool;
	
	//Constructor
	public AndroidAudio(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		assets = activity.getAssets();
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); 
	}
	
	//Method
	@Override
	public Music newMusic(String fileName) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			return new AndroidMusic(assetDescriptor);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load music '" + fileName +"'");
		}
	}
	
	@Override
	public Sound newSound(String fileName) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load sound '" + fileName +"'");
		}
	}
}
