package com.battleship.model.equipment

import com.badlogic.gdx.audio.Sound
import com.battleship.model.GameObject
import com.battleship.utility.SoundEffects

abstract class Equipment : GameObject() {
    abstract var searchRadius: Int
    abstract var uses: Int
    abstract var name: String
    abstract var active: Boolean
    abstract var sound: Sound

    fun hasMoreUses(): Boolean {
        return uses > 0
    }

    fun use() {
        uses--
    }

    fun playSound(volume: Float = 0.8f) {
        SoundEffects.play(sound, volume)
    }
}
