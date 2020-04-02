package com.battleship.model.equipment

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class Shovel : Equipment() {
    override var searchRadius: Int = 0
    override var uses: Int = 100
    override var name: String = "Shovel"
    override var active: Boolean = false
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/dirt_sound.mp3"))
}
