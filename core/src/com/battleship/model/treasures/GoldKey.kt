package com.battleship.model.treasures

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

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
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/key_sound.mp3"))
    override var type: TreasureType = TreasureType.GOLDKEY
    override var sprite: Sprite = if (rotate) {
        Sprite(Texture("images/treasures/rotatedKey.png"))
    } else {
        Sprite(Texture("images/treasures/key.png"))
    }

    /**
     * If rotate is true init block triggers rotateTreasure after constructor setup
     */
    init {
        if (rotate) rotateTreasure()
    }
}
