package com.battleship.model.ships

import com.badlogic.gdx.math.Vector2

class MediumShip(position: Vector2) : Ship(position) {
    override var dimension: Vector2 = Vector2(1f, 3f)
    override var name: String = "MediumShip"
    override var health: Int = 3
}
