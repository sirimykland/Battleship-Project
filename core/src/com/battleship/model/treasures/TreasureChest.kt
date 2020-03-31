package com.battleship.model.treasures

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

class TreasureChest(position: Vector2, rotate: Boolean) : Treasure(position, rotate) {
    override var dimension: Vector2 = Vector2(2f, 2f)
    override var name: String = "Treasure chest"
    override var health: Int = 4
    override var sprite: Sprite = Sprite(Texture("badlogic.jpg"))

    init {
        if (rotate) rotateTreasure()
    }
}
