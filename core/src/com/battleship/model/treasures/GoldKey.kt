package com.battleship.model.treasures

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.SoundEffects
import com.battleship.utility.TextureLibrary

/**
 * Inherits behavior from [Treasure]
 *
 * @constructor inherited from [Treasure]:
 * @property position: Vector2 - position of treasure on board grid
 * @property rotate: Boolean - describes if treasure is rotated, default: false
 */
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

    /**
     * If rotate is true init block triggers rotateTreasure after constructor setup
     */
    init {
        if (rotate) rotateTreasure()
    }
}
