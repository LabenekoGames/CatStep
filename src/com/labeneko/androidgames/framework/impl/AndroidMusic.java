package com.labeneko.androidgames.framework.impl;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import com.labeneko.androidgames.framework.Music;

public class AndroidMusic implements Music, OnCompletionListener {
	//Field
	MediaPlayer mediaPlayer;
	boolean isPrepared = false;
	
	//Constructor
	public AndroidMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(), assetDescriptor.getStartOffset(), assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		} catch(Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}
	
	//Method
	@Override
	public void dispose() {
		if(mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();
	}
	
	@Override
	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}
	
	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}
	
	@Override
	public boolean isStopped() {
		return !isPrepared;
	}
	
	@Override
	public void pause() {
		if(mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}
	
	@Override
	public void play() {
		if(mediaPlayer.isPlaying())
			return;
		
		try {
			synchronized (this) {
				if (!isPrepared) {
					mediaPlayer.prepare();
				}
				mediaPlayer.start();
			}
		} catch (IllegalStateException e) {
			Log.e("ERROR(play_music)", e.getMessage());
		} catch (IOException e) {
			Log.e("ERROR(play_music)", e.getMessage());
		}
	}
	
	@Override
	public void setLooping(boolean isLooping) {
		mediaPlayer.setLooping(isLooping);
	}
	
	@Override
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
	}
	
	@Override
	public void stop() {
		mediaPlayer.stop();
		synchronized (this) {
			isPrepared = false;
		}
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		synchronized (this) {
			isPrepared = false;
		}		
	}

}
