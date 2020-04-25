package com.battleship.model.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font
import kotlin.math.round

/**
 * Inherits behavior from [GuiElement]
 *
 * @constructor
 * @param text: String - The text that should be drawn
 * @param font: BitmapFont - The font that should be used to draw the text
 * @param align: Int - Sets text alignment, should be a constant from [Align]
 * @param textWrap: Boolean - Whether the text should wrap to the next line if it doesn't fit within
 * the horizontal bounds of the [GuiObject] it is a part of
 * @param margin: Float - The margin on the left and right side of the text
 *
 */
class Text(
    private val text: String,
    private val font: BitmapFont = Font.SMALL_WHITE,
    private val align: Int = Align.center,
    private val textWrap: Boolean = true,
    margin: Float = 0.5f
) : GuiElement() {

    private val margin = round(margin * Gdx.graphics.width / 100)

    private val glyphLayout = GlyphLayout()

    /**
     * Draws the text
     * @param batch: SpriteBatch - The SpriteBatch object used to draw the text
     * @param position: Vector2 - The position on the screen where the image should be text
     * @param size: Vector2 - The size of the text
     */
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
