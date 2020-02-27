package com.battleship.model.ships

import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

abstract class Ship : GameObject() {
    abstract val dimension: Vector2

    // TODO
}
