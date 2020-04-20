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
class GoldCoin(position: Vector2, rotate: Boolean) : Treasure(position, rotate) {
    override var dimension: Vector2 = Vector2(1f, 1f)
    override var name: String = "Gold coin"
    override var health: Int = 1
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/coin_sound.mp3"))
    override var sprite: Sprite = Sprite(Texture("images/treasures/coin.png"))
    override var type: TreasureType = TreasureType.GOLDCOIN

    /**
     * If rotate is true init block triggers rotateTreasure after constructor setup
     */
    init {
        if (rotate) rotateTreasure()
    }
}
