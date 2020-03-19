package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.battleship.controller.state.MainMenuState
import com.battleship.utility.Font

class BattleshipGame : Game() {
    companion object{
        var music: Music? = null
    }

    override fun create() {
        if (Gdx.files.internal("music.mp3").exists()) {
            music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"))
            music?.isLooping = true;
            music?.volume = 0.5f;
            music?.play();
        }

        GameStateManager.push(MainMenuState())
    }

    override fun dispose() {
        Font.dispose()
    }

    override fun render() {
        super.render()
        GameStateManager.render()
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
