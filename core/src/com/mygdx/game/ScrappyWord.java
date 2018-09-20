package com.mygdx.game;

import com.badlogic.gdx.Game;

public class ScrappyWord extends Game {

	/*
	*  initialize game and set screen to main menu
	 */
	public void create() {
		// set screen
		setScreen(new MainMenuScreen(this));
		//setScreen(new GameScreen(this));

	}
}
