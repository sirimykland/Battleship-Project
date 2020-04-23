package com.battleship.controller.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.RectangleUtil

/**
 * Click handler class inheriting from [InputAdapter].
 *
 * @constructor
 * @property position: Vector2
 * @property size: Vector2
 * @property onClick: () -> Boolean
 */
class ClickHandler(val position: Vector2, val size: Vector2, val onClick: () -> Boolean) : InputAdapter() {

    /**
     * Called when the screen was touched or a mouse button was pressed.
     *
     * @param screenX: Int
     * @param screenY: Int
     * @param pointer: Int
     * @param button: Int
     * @return Boolean
     */
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (RectangleUtil.fromVectors(position, size).contains(screenX.toFloat(), Gdx.graphics.height - screenY.toFloat())) {
            return onClick()
        }
        return false
    }
}
