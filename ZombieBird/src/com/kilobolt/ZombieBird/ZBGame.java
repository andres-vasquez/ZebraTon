package com.kilobolt.ZombieBird;

import com.badlogic.gdx.Game;
import com.kilobolt.Screens.SplashScreen;
import com.kilobolt.ZBHelpers.AssetLoader;

public class ZBGame extends Game {

	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}

	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

}