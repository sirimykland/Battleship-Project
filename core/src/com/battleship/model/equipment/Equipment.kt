package com.battleship.model.equipment

import com.badlogic.gdx.audio.Sound
import com.battleship.BattleshipGame
import com.battleship.model.GameObject

abstract class Equipment : GameObject() {
    abstract var searchRadius: Int
    abstract var uses: Int
    abstract var name: String
    abstract var active: Boolean
    abstract var sound: Sound

    /**
     * Checks if integer uses is greater than 0
     *
     * @return boolean true or false
     */
    fun hasMoreUses(): Boolean {
        return uses > 0
    }

    /**
     * Decrement integer uses with 1.
     */
    fun use() {
        uses--
    }

    /**
     * Play sound relevant to equipment
     *
     * @param volume of the sound
     */
    fun playSound(volume: Float) {
        if (BattleshipGame.soundOn) {
            sound.stop()
            sound.play(volume)
        }
    }
}
