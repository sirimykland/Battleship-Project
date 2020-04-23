package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.Palette
// TODO Karl
class Border(
    private val color: TextureRegion = Palette.WHITE,
    private val widthTop: Float = 5f,
    private val widthRight: Float = 5f,
    private val widthBottom: Float = 5f,
    private val widthLeft: Float = 5f
) : GuiElement() {

    constructor(color: TextureRegion = Palette.WHITE, width: Float = 5f) : this(color, width, width, width, width)

    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        batch.draw(color, position.x, position.y + size.y - widthTop, size.x, widthTop)
        batch.draw(color, position.x + size.x - widthRight, position.y, widthRight, size.y)
        batch.draw(color, position.x, position.y, size.x, widthBottom)
        batch.draw(color, position.x, position.y, widthLeft, size.y)
    }

    override val zIndex: Int = -1
}
