package com.battleship.utility

import com.badlogic.gdx.Graphics
import com.badlogic.gdx.math.Vector2

/**
 * Gdx.graphic utility class
 */
object GdxGraphicsUtil {

    /**
     * Gets the screen size of the device used as a 2D vector
     *
     * @return Vector2
     */
    fun Graphics.size(): Vector2 {
        return Vector2(
                this.width.toFloat(),
                this.height.toFloat()
        )
    }

    /**
     * Gets the position of the board on the screen as a Vector2
     * This is the corner position aka the start position for drawing.
     *
     * @return Vector2
     */
    fun Graphics.boardPosition(): Vector2 {
        return Vector2(
                this.width.toFloat() * 0.05f,
                this.height / 2f - this.boardWidth() / 2f
        )
    }

    /**
     * Gets the Width of the board relevant to the screen
     *
     * @return Float - width of the board
     */
    fun Graphics.boardWidth(): Float {
        return this.width.toFloat() * 0.9f
    }

    /**
     * Position of the equipmentSet box as a vector.
     * This is the corner position aka the start position for drawing.
     *
     * @return Vector2
     */
    fun equipmentSetPosition(): Vector2 {
        return Vector2(5f, 2f)
    }

    /**
     * Size of the equipmentSet box as a vector.
     *
     * @return Vector2
     */
    fun equipmentSetSize(): Vector2 {
        return Vector2(88f, 10f)
    }
}
