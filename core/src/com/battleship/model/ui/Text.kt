package com.battleship.model.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font

class Text(
    private val text: String,
    private val font: BitmapFont = Font.SMALL_WHITE,
    private val align: Int = Align.center,
    private val textWrap: Boolean = true,
    private val margin: Float = 10f
) : GuiElement() {
    private val glyphLayout = GlyphLayout()

    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        glyphLayout.setText(font, text, Color.BLACK, size.x - margin * 2, align, textWrap)
        font.draw(
            batch,
            text,
            position.x + margin,
            position.y + size.y / 2f + glyphLayout.height / 2f,
            size.x - margin * 2,
            align,
            textWrap)
    }
    override val zIndex: Int = 0
}
