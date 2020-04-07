package com.battleship.model.treasures

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

class Boot(position: Vector2, rotate: Boolean) : Treasure(position) {
    override var dimension: Vector2 = Vector2(1f, 2f)
    override var name: String = "Old stinking boot"
    override var health: Int = 2
    override var sprite: Sprite = Sprite(Texture("images/boot.png"))
    override var type: TreasureType = TreasureType.BOOT

    init {
        if (rotate) rotateTreasure()
    }
}
