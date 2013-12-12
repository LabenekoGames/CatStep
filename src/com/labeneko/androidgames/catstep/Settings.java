package com.labeneko.androidgames.catstep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

import com.labeneko.androidgames.framework.FileIO;

public class Settings {
	//Field
	public static boolean soundEnabled = true;
	public static int stage = 1;
	public static float level = 1.0f + (stage - 1) * 0.1f;
	public static int[] top5scores = new int[] {100, 80, 60, 40, 20};
	public static String settingFile = ".catjumper";
	
	//method
	public static void load(FileIO files) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(files.readFile(settingFile)));
			soundEnabled = Boolean.parseBoolean(in.readLine());
			for(int i = 0; i < 5; i++)
				top5scores[i] = Integer.parseInt(in.readLine());
		} catch(IOException e) {
			Log.e("(load)ERROR1", e.getMessage());
		} catch(NumberFormatException e) {
			Log.e("(load)ERROR2", e.getMessage());
		} finally {
			try {
				if(in != null)
					in.close();
			} catch (IOException e) {
				Log.e("(load3)ERROR", e.getMessage());
			}
		}
	}
	
	public static void save(FileIO files) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(files.writeFile(settingFile)));
			out.write(Boolean.toString(soundEnabled));
			out.newLine();
			for(int i = 0; i < 5; i++) {
				out.write(Integer.toString(top5scores[i]));
				out.newLine();
			}
		} catch (IOException e) {
			Log.e("(save)ERROR1", e.getMessage());
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) {
				Log.e("(save)ERROR2", e.getMessage());
			}
		}
	}
	
	public static void addScore(int score) {
		for(int i = 0; i < 5; i++) {
			if(top5scores[i] < score) {
				for(int j = 4; j > i; j--)
					top5scores[j] = top5scores[j-1];
				top5scores[i] = score;
				break;
			}
		}
	}
}
