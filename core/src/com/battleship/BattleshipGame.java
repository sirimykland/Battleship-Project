package com.battleship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.battleship.controller.state.PlayState;

public class BattleshipGame extends Game {


	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		GameStateManager.INSTANCE.push(new PlayState());
	}

	// TODO make View render and remove this
	/*@Override
	public void render () {

		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}*/
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
