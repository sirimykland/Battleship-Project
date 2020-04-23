package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.state.MainMenuState
import com.battleship.controller.state.UsageGuideState
import com.battleship.utility.Font
import com.battleship.utility.Palette
import com.battleship.utility.SoundEffects
import com.battleship.utility.TextureLibrary

class BattleshipGame(private val controller: FirebaseController) : Game() {

    lateinit var prefs: Preferences

    companion object {
        var music: Music? = null
        var soundOn: Boolean = true
    }

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
        prefs = Gdx.app.getPreferences("firsttimeopen")
        prefs.putBoolean("lock", true)
        print("prefs: " + prefs.getBoolean("lock", true))
        if (prefs.getBoolean("lock", true)) {
            prefs.putBoolean("lock", false)
            prefs.flush()
            GSM.push(UsageGuideState(controller))
        } else {
            GSM.push(MainMenuState(controller))
        }
        controller.addPendingGamesListener { pendingGames ->
            GSM.pendingGames = pendingGames
        }
        music?.play()
    }

    override fun dispose() {
        Font.dispose()
        SoundEffects.dispose()
        Palette.dispose()
        TextureLibrary.dispose()
    }

    override fun render() {
        super.render()
        GSM.render()
        GSM.update(Gdx.graphics.deltaTime)
    }
}
