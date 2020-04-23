package com.battleship.model.equipment

import com.badlogic.gdx.audio.Sound
import com.battleship.utility.SoundEffects

/**
 *  Inherits behavior from [Equipment]
 */
class BigEquipment : Equipment() {
    override var searchRadius: Int = 1
    override var uses: Int = 10
    override var name: String = "Big equipment"
    override var active: Boolean = false
    override var sound: Sound = SoundEffects.BIGEQUIPMENT
}
