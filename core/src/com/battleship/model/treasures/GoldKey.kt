package com.battleship.model.treasures

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.SoundEffects
import com.battleship.utility.TextureLibrary

class GoldKey(position: Vector2, rotate: Boolean) : Treasure(position, rotate) {
    override var dimension: Vector2 = Vector2(1f, 2f)
    override var name: String = "Shiny gold key"
    override var health: Int = 2
    override var sound: Sound = SoundEffects.GOLDKEY
    override var type: TreasureType = TreasureType.GOLDKEY
    override var sprite: Sprite = if (rotate) {
        Sprite(TextureLibrary.GOLDKEY_ROTATED)
    } else {
        Sprite(TextureLibrary.GOLDKEY)
    }

    init {
        if (rotate) rotateDimensions()
    }

    override fun rotate() {
        rotate = !rotate
        sprite = if (rotate) {
            Sprite(TextureLibrary.GOLDKEY_ROTATED)
        } else {
            Sprite(TextureLibrary.GOLDKEY)
        }
        rotateDimensions()
    }
}
