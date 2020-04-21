package com.battleship.model.equipment

import com.badlogic.gdx.audio.Sound
import com.battleship.utility.SoundEffects

class MetalDetector : Equipment() {
    override var searchRadius: Int = 1
    override var uses: Int = 1
    override var name: String = "Metal detector"
    override var active: Boolean = false
    override var sound: Sound = SoundEffects.METAL_DETECTOR
}
