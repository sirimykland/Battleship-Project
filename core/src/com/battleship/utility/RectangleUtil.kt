package com.battleship.utility

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

object RectangleUtil {
    fun fromVectors(position: Vector2, size: Vector2): Rectangle {
        return Rectangle(position.x, position.y, size.x, size.y)
    }
}
