package com.battleship.model.treasures

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.TextureLibrary

class GoldKey(position: Vector2, rotate: Boolean) : Treasure(position, rotate) {
    override var dimension: Vector2 = Vector2(1f, 2f)
    override var name: String = "Shiny gold key"
    override var health: Int = 2
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/key_sound.mp3"))
    override var type: TreasureType = TreasureType.GOLDKEY
    override var sprite: Sprite = if (rotate) {
        Sprite(TextureLibrary.GOLDKEY_ROTATED)
    } else {
        Sprite(TextureLibrary.GOLDKEY)
    }

    init {
        if (rotate) rotateTreasure()
    }
}
