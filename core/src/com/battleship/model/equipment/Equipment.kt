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

    fun hasMoreUses(): Boolean {
        return uses > 0
    }

    fun use() {
        uses--
    }

    fun playSound(volume: Float) {
        if (BattleshipGame.soundOn) {
            sound.stop()
            sound.play(volume)
        }
    }

}
