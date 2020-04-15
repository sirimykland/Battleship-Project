package com.battleship.model.equipment

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class BigEquipment : Equipment() {
    override var searchRadius: Int = 1
    override var uses: Int = 10
    override var name: String = "Big equipment"
    override var active: Boolean = false
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/bomb_sound.mp3"))
}
