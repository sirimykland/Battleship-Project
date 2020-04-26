package com.battleship.model.equipment

import com.badlogic.gdx.audio.Sound
import com.battleship.utility.SoundEffects

/**
 *  Inherits behavior from [Equipment]
 */
class Dynamite : Equipment() {
    override var searchRadius: Int = 1
    override var uses: Int = 2
    override var name: String = "Dynamite"
    override var active: Boolean = false
    override var sound: Sound = SoundEffects.DYNAMITE
}
