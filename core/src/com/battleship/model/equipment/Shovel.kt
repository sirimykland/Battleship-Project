package com.battleship.model.equipment

import com.badlogic.gdx.audio.Sound
import com.battleship.utility.SoundEffects

/**
 *  Inherits behavior from [Equipment]
 */
class Shovel : Equipment() {
    override var searchRadius: Int = 0
    override var uses: Int = 100
    override var name: String = "Shovel"
    override var active: Boolean = false
    override var sound: Sound = SoundEffects.SHOVEL
}
