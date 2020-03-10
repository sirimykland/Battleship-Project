package com.battleship.model.ships

import com.badlogic.gdx.math.Vector2

class SmallShip(position: Vector2) : Ship(position) {
    override var dimension: Vector2 = Vector2(1f, 2f)
    override var name: String = "SmallShip"
    override var health: Int = 2
}
