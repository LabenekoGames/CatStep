package com.labeneko.androidgames.catstep;

import com.labeneko.androidgames.framework.Music;
import com.labeneko.androidgames.framework.Sound;
import com.labeneko.androidgames.framework.gl.Animation;
import com.labeneko.androidgames.framework.gl.TextWriter;
import com.labeneko.androidgames.framework.gl.Texture;
import com.labeneko.androidgames.framework.gl.TextureArea;
import com.labeneko.androidgames.framework.impl.GLGameActivity;

public class Assets {
	//Field
	public static Texture items;
	
	public static TextureArea doorBg;
	public static TextureArea room1Bg;
	public static TextureArea room2Bg;
	public static TextureArea mainMenu;
	public static TextureArea pauseMenu;
	public static TextureArea ready;
	public static TextureArea gameOver;
	public static TextureArea ScoreRegion;
	public static TextureArea titlelogo;
	public static TextureArea soundOn;
	public static TextureArea soundOff;
	public static TextureArea arrow;
	public static TextureArea pause;
	public static TextureArea spring;
	public static TextureArea castle;
	public static TextureArea catCrash;
	public static TextureArea step;

	public static TextureArea[] cheeses = new TextureArea[4];
	public static TextureArea[] fishes = new TextureArea[4];
	public static TextureArea[] catJumps = new TextureArea[2];
	public static TextureArea[] catFalls = new TextureArea[2];
	public static TextureArea[] rats = new TextureArea[2];
	public static TextureArea[] breakingSteps = new TextureArea[4];

	public static Animation cheeseAnim;
	public static Animation fishAnim;
	public static Animation catJumpAnim;
	public static Animation catFallAnim;
	public static Animation ratAnim;
	public static Animation breakingStepAnim;

	public static TextWriter textWriter;

	public static Music openingMusic;
	public static Music playingMusic;

	public static Sound jumpSound;
	public static Sound highJumpSound;
	public static Sound crashSound;
	public static Sound getSound;
	public static Sound clickSound;
	public static Sound openDoorSound;

	//Method
	public static void load(GLGameActivity glGame) {
		items = new Texture(glGame, "images/cat_step_atlas.png", 1024, 1024);
		
		doorBg = new TextureArea(items, 0, 512, 320, 480);
		room1Bg = new TextureArea(items, 512, 0, 320, 480);
		room2Bg = new TextureArea(items, 512, 512, 320, 480);
		mainMenu = new TextureArea(items, 192, 256, 160, 96);
		pauseMenu = new TextureArea(items, 224, 128, 192, 96);
		ready = new TextureArea(items, 320, 215, 192, 48);
		gameOver = new TextureArea(items, 369, 256, 128, 96);
		ScoreRegion = new TextureArea(items, 192, 256 + 96 / 2, 160, 96 / 2);
		titlelogo = new TextureArea(items, 0, 384, 274, 128);
		soundOff = new TextureArea(items, 0, 0, 64, 64);
		soundOn = new TextureArea(items, 64, 0, 64, 64);
		arrow = new TextureArea(items, 0, 64, 64, 64);
		pause = new TextureArea(items, 64, 64, 64, 64);
		spring = new TextureArea(items, 128, 0, 32, 32);
		castle = new TextureArea(items, 128, 160, 64, 64);
		catCrash = new TextureArea(items, 128, 128, 32, 32);
		step = new TextureArea(items, 64, 160, 64, 16);
		cheeses[0] = new TextureArea(items, 128, 32, 32, 32);
		cheeses[1] = new TextureArea(items, 128, 32, 32, 32);
		cheeses[2] = new TextureArea(items, 128, 32, 32, 32);
		cheeses[3] = new TextureArea(items, 128, 32, 32, 32);
		fishes[0] = new TextureArea(items, 128, 64, 32, 32);
		fishes[1] = new TextureArea(items, 160, 64, 32, 32);
		fishes[2] = new TextureArea(items, 192, 64, 32, 32);
		fishes[3] = new TextureArea(items, 160, 64, 32, 32);
		catJumps[0] = new TextureArea(items, 0, 128, 32, 32);
		catJumps[1] = new TextureArea(items, 32, 128, 32, 32);
		catFalls[0] = new TextureArea(items, 64, 128, 32, 32);
		catFalls[1] = new TextureArea(items, 96, 128, 32, 32);
		rats[0] = new TextureArea(items, 0, 160, 32, 32);
		rats[1] = new TextureArea(items, 32, 160, 32, 32);
		breakingSteps[0] = new TextureArea(items, 64, 160, 64, 16);
		breakingSteps[1] = new TextureArea(items, 64, 176, 64, 16);
		breakingSteps[2] = new TextureArea(items, 64, 192, 64, 16);
		breakingSteps[3] = new TextureArea(items, 64, 208, 64, 16);

		cheeseAnim = new Animation(cheeses, 0.2f);
		fishAnim = new Animation(fishes, 0.2f);
		catJumpAnim = new Animation(catJumps, 0.2f);
		catFallAnim = new Animation(catFalls, 0.2f);
		ratAnim = new Animation(rats, 0.2f);
		breakingStepAnim = new Animation(breakingSteps, 0.4f);

		textWriter = new TextWriter(items, 224, 0, 16, 16, 20);

		openingMusic = glGame.getAudio().newMusic("music/opening.ogg");
		openingMusic.setLooping(true);
		openingMusic.setVolume(0.3f);
		playMusic();

		jumpSound = glGame.getAudio().newSound("sounds/jump.ogg");
		highJumpSound = glGame.getAudio().newSound("sounds/spring.ogg");
		crashSound = glGame.getAudio().newSound("sounds/rat.ogg");
		getSound = glGame.getAudio().newSound("sounds/get.ogg");
		clickSound = glGame.getAudio().newSound("sounds/click.ogg");
		openDoorSound = glGame.getAudio().newSound("sounds/opendoor.ogg");
	}

	public static void reload() {
		items.reload();
		playMusic();
		if (Settings.soundEnabled) {
			openingMusic.play();
		}
	}

	public static void playMusic() {
		if (Settings.soundEnabled) {
			openingMusic.play();
		}
	}

	public static void pauseMusic() {
		openingMusic.pause();
	}

	public static void playSound(Sound sound) {
		if (Settings.soundEnabled) {
			sound.play(0.3f);
		}
	}

}
