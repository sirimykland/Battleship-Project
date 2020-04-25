package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
/**
 * Abstract class for elements that make up a [GuiObject].
 */
abstract class GuiElement {
    /**
     * Draws the element
     * @param batch: SpriteBatch - The SpriteBatch object used to draw the element
     * @param position: Vector2 - The position on the screen where the element should be drawn
     * @param size: Vector2 - The size of the element
     */
    abstract fun draw(batch: SpriteBatch, position: Vector2, size: Vector2)

    /**
     * @property zIndex: Int - Determines the order elements should be drawn, higher value is drawn last
     */
    abstract val zIndex: Int
}
