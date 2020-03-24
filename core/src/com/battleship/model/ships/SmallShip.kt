package com.battleship.model.ships

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

class SmallShip(position: Vector2, rotate: Boolean) : Ship(position, rotate) {

    override var dimension: Vector2 = Vector2(1f, 2f)
    override var name: String = "SmallShip"
    override var health: Int = 2
    override var sprite: Sprite = Sprite(Texture("badlogic.jpg"))

    init {
        if (rotate) rotateShip()
    }
}
