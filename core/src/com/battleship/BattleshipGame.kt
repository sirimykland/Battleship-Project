package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.state.MainMenuState
import com.battleship.utility.Font
import com.battleship.utility.Palette
import com.battleship.utility.SoundEffects
import com.battleship.utility.TextureLibrary

/**
 * Main class for BattleshipGame. Inherits behaviour LibGdx's from [Game].
 *
 * @constructor
 * @property controller: FirebaseController - position of treasure on board grid
 */
class BattleshipGame(private val controller: FirebaseController) : Game() {
    /**
     * Music and sound settings as a Singleton, properties can be accessed
     * directly via the name of the containing class.
     */
    companion object {
        var music: Music? = null
        var soundOn: Boolean = true
    }

    /**
     * Called once when the Application is first created.
     */
    override fun create() {
        SoundEffects.load()
        Font.load()
        Palette.load()
        TextureLibrary.load()
        if (Gdx.files.internal("audio/music.mp3").exists()) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music.mp3"))
            music?.isLooping = true
            music?.volume = 0.2f
        }
        controller.addPendingGamesListener { pendingGames ->
            GSM.pendingGames = pendingGames
        }
        GSM.push(MainMenuState(controller))
        music?.play()
    }

    /**
     * Called once when the Application is destroyed.
     */
    override fun dispose() {
        Font.dispose()
        SoundEffects.dispose()
        Palette.dispose()
        TextureLibrary.dispose()
    }

    /**
     * Called when the Application should render itself.
     */
    override fun render() {
        super.render()
        GSM.render()
        GSM.update(Gdx.graphics.deltaTime)
    }
}
