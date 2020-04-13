package com.battleship.model.equipment

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class MetalDetector : Equipment() {
    override var searchRadius: Int = 1
    override var uses: Int = 1
    override var name: String = "Metal detector"
    override var active: Boolean = false
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/radar_sound.mp3"))
}
