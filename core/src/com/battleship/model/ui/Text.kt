package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font

class Text(
    private val text: String,
    private val font: BitmapFont = Font.SMALL_WHITE,
    private val align: Int = Align.center,
    private val textWrap: Boolean = true
) : GuiElement() {
    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        font.draw(batch, text, position.x + 10f, position.y + size.y / 2, size.x - 20, align, textWrap)
    }

    override val zIndex: Int = 0
}
