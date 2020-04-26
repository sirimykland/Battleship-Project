package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.Palette
/**
 * Inherits behavior from [GuiElement]
 *
 * @constructor
 * @param color: TextureRegion - Image that should be used as background, most likely a constant from [Palette]
 */
class Background(val color: TextureRegion = Palette.BLACK) : GuiElement() {

    /**
     * Draws the background
     * @param batch: SpriteBatch - The SpriteBatch object used to draw the background
     * @param position: Vector2 - The position on the screen where the background should be drawn
     * @param size: Vector2 - The size of the background
     */
    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        batch.draw(color, position.x, position.y, size.x, size.y)
    }

    override val zIndex: Int = -2
}
