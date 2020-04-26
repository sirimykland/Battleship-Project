package com.battleship.utility

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

/**
 * Gdx.math.Rectangle utility class
 */
object RectangleUtil {

    /**
     * Returns a rectangle
     *
     * @param position: Vector2 - start coordinates on the screen
     * @param size: Vector2 - width and height of the Rectangle
     * @return Rectangle
     */
    fun fromVectors(position: Vector2, size: Vector2): Rectangle {
        return Rectangle(position.x, position.y, size.x, size.y)
    }
}
