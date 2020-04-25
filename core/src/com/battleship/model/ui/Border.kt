package com.battleship.model.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.Palette
import kotlin.math.round

/**
 * Inherits behavior from [GuiElement]
 *
 * @constructor
 * @param color: TextureRegion - Image that should be used as background, most likely a constant from [Palette],
 * @param widthTop: Float - The width of the border above the [GuiObject] this is a part of.
 * @param widthRight: Float - The width of the border on the right side of the [GuiObject] this is a part of.
 * @param widthBottom: Float - The width of the border below the [GuiObject] this is a part of.
 * @param widthLeft: Float - The width of the border on the left side of the [GuiObject] this is a part of.
 */
class Border(
    private val color: TextureRegion = Palette.WHITE,
    widthTop: Float = 0f,
    widthRight: Float = 0f,
    widthBottom: Float = 0f,
    widthLeft: Float = 0f
) : GuiElement() {

    private val widthTop = round(widthTop * Gdx.graphics.width / 100)
    private val widthRight = round(widthRight * Gdx.graphics.width / 100)
    private val widthBottom = round(widthBottom * Gdx.graphics.width / 100)
    private val widthLeft = round(widthLeft * Gdx.graphics.width / 100)

    /**
     * @constructor
     * @param color: TextureRegion - Image that should be used as background, most likely a constant from [Palette],
     * @param width: Float - The width of the border around the [GuiObject] this is a part of.
     */
    constructor(color: TextureRegion = Palette.WHITE, width: Float = 0.7f) : this(color, width, width, width, width)

    /**
     * Draws the border
     * @param batch: SpriteBatch - The SpriteBatch object used to draw the border
     * @param position: Vector2 - The position on the screen where the border should be drawn
     * @param size: Vector2 - The size of the border
     */
    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        batch.draw(color, position.x, position.y + size.y - widthTop, size.x, widthTop)
        batch.draw(color, position.x + size.x - widthRight, position.y, widthRight, size.y)
        batch.draw(color, position.x, position.y, size.x, widthBottom)
        batch.draw(color, position.x, position.y, widthLeft, size.y)
    }

    override val zIndex: Int = -1
}
