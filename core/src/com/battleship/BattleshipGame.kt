package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.state.MainMenuState
import com.battleship.controller.state.UsageGuideState
import com.battleship.utility.Font

class BattleshipGame(private val controller: FirebaseController) : Game() {

    lateinit var prefs: Preferences

    companion object {
        var music: Music? = null
        var soundOn: Boolean = true
    }
    
    override fun create() {
        if (Gdx.files.internal("audio/music.mp3").exists()) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music.mp3"))
            music?.isLooping = true
            music?.volume = 0.2f
            music?.play()
        }
        prefs = Gdx.app.getPreferences("firsttimeopen")
        print(prefs)
        if (prefs.getBoolean("lock",true) ) {
            prefs.putBoolean("lock", false);
            prefs.flush();
            GSM.push(UsageGuideState(controller))
        } else {
            GSM.push(MainMenuState(controller))
        }
        controller.addPendingGamesListener { pendingGames ->
            GSM.pendingGames = pendingGames
        }
        GSM.push(MainMenuState(controller))
    }

    override fun dispose() {
        Font.dispose()
    }

    override fun render() {
        super.render()
        GSM.render()
        GSM.update(Gdx.graphics.deltaTime)
    }
}
