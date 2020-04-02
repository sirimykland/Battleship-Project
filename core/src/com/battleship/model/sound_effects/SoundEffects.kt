package com.battleship.model.sound_effects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class SoundEffects {

    var dirt: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/dirt_sound.mp3"))
    var coin: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/coin_sound.mp3"))
    var hit: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/hit_sound.mp3"))

    var sounds: List<Sound> = listOf(
        dirt,
        coin,
        hit
    )

    fun stopSounds() {
        for (sound in sounds) {
            sound.stop()
        }
    }

    fun playDirt(){
        stopSounds()
        dirt.play(0.4f)
    }

    fun playCoin(){
        stopSounds()
        coin.play(0.8f)
    }

    fun playHit(){
        stopSounds()
        hit.play(1.5f)
    }

}


