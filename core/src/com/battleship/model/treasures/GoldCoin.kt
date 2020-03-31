package com.battleship.model.treasures

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

class GoldCoin(position: Vector2, rotate: Boolean) : Treasure(position, rotate) {
    override var dimension: Vector2 = Vector2(1f, 1f)
    override var name: String = "Gold coin"
    override var health: Int = 1
    override var sprite: Sprite = Sprite(Texture("badlogic.jpg"))

    init {
        if (rotate) rotateTreasure()
    }
}
