package com.battleship.model.soundeffects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.battleship.BattleshipGame

object SoundEffects {

    var hit: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/hit_sound.mp3"))
    var victory: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/victory_sound.mp3"))
    var losing: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/losing_sound.mp3"))
    var click: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/click_sound.mp3"))
    var alreadyPlayed: Boolean = false

    fun playHit(volume: Float) {
        if (BattleshipGame.soundOn) {
            hit.stop()
            hit.play(volume)
        }
    }

    fun playClick(volume: Float) {
        if (BattleshipGame.soundOn) {
            click.stop()
            click.play(volume)
        }
    }

    fun playVictory(volume: Float) {
        if (BattleshipGame.soundOn && !alreadyPlayed)  {
            victory.play(volume)
            alreadyPlayed = true
        }
    }

    fun playLosing(volume: Float) {
        if (BattleshipGame.soundOn && !alreadyPlayed) {
            losing.play(volume)
            alreadyPlayed = true
        }
    }
}
