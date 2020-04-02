package com.battleship.model.sound_effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class SoundEffects {

    var hit: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/hit_sound.mp3"))

    fun playHit(volume: Float) {
        hit.stop()
        hit.play(volume)
    }

}


