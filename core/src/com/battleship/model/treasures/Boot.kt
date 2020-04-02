package com.battleship.model.treasures

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

class Boot(position: Vector2, rotate: Boolean) : Treasure(position) {
    override var dimension: Vector2 = Vector2(1f, 1f)
    override var name: String = "Old stinking boot"
    override var health: Int = 1
    override var sprite: Sprite = Sprite(Texture("images/boot.png"))

    init {
        if (rotate) rotateTreasure()
    }
}
