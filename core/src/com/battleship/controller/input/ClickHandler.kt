package com.battleship.controller.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.RectangleUtil

/**
 * Click handler class inheriting from [InputAdapter].
 *
 * @constructor
 * @param position: Vector2
 * @param size: Vector2
 * @property onClick: () -> Boolean
 */
class ClickHandler(position: Vector2, size: Vector2, val onClick: () -> Boolean) : InputAdapter() {
    private val boundary = RectangleUtil.fromVectors(position, size)

    /**
     * Called when the screen was touched or a mouse button was pressed.
     * Invokes [onClick] if the click/touch was within the boundaries of [boundary]
     *
     * @param screenX: Int - The x coordinate, origin is in the upper left corner
     * @param screenY: Int - The y coordinate, origin is in the upper left corner
     * @param pointer: Int - the pointer for the event.
     * @param button: Int - the button
     * @return Boolean - whether the input was processed
     */
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (boundary.contains(screenX.toFloat(), Gdx.graphics.height - screenY.toFloat())) {
            return onClick()
        }
        return false
    }
}
