package com.battleship.model.sound_effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class SoundEffects {

    var hit: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/hit_sound.mp3"))
    var victory: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/victory_sound.mp3"))
    var losing: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/losing_sound.mp3"))

    fun playHit(volume: Float) {
        hit.stop()
        hit.play(volume)
    }

    fun playVictory(volume: Float) {
        victory.play(volume)
    }

    fun playLosing(volume: Float) {
        losing.play(volume)
    }
}


