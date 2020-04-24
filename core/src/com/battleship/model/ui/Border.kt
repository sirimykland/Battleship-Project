package com.battleship.model.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.Palette
import kotlin.math.round

class Border(
    private val color: TextureRegion = Palette.WHITE,
    widthTop: Float = 2f,
    widthRight: Float = 2f,
    widthBottom: Float = 2f,
    widthLeft: Float = 2f
) : GuiElement() {
    private val widthTop = round(widthTop * Gdx.graphics.width / 100)
    private val widthRight = round(widthRight * Gdx.graphics.width / 100)
    private val widthBottom = round(widthBottom * Gdx.graphics.width / 100)
    private val widthLeft = round(widthLeft * Gdx.graphics.width / 100)

    constructor(color: TextureRegion = Palette.WHITE, width: Float = 0.7f) : this(color, width, width, width, width)

    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        batch.draw(color, position.x, position.y + size.y - widthTop, size.x, widthTop)
        batch.draw(color, position.x + size.x - widthRight, position.y, widthRight, size.y)
        batch.draw(color, position.x, position.y, size.x, widthBottom)
        batch.draw(color, position.x, position.y, widthLeft, size.y)
    }

    override val zIndex: Int = -1
}
