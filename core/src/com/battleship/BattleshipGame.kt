package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.battleship.controller.state.MainMenuState
import com.battleship.utility.Font

class BattleshipGame : Game() {
    var music: Music? = null

    override fun create() {
        val music =
            Gdx.audio.newMusic(Gdx.files.internal("music.mp3"))
        music.isLooping = true;
        music.volume = 0.1f;
        music.play();

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
