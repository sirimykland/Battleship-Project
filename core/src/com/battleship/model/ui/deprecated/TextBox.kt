package com.battleship.model.ui.deprecated

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font
import com.battleship.utility.Palette

@Deprecated("Use GuiObject instead")
class TextBox(
    position: Vector2,
    size: Vector2,
    private val text: String,
    private val font: BitmapFont = Font.MEDIUM_BLACK,
    borderColor: TextureRegion = Palette.WHITE,
    color: TextureRegion = Palette.LIGHT_BLUE
) :
    MenuItem(position, size, borderColor, color) {

    constructor(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        text: String,
        font: BitmapFont = Font.MEDIUM_BLACK,
        borderColor: TextureRegion = Palette.WHITE,
        color: TextureRegion = Palette.LIGHT_BLUE
    ) :
        this(Vector2(posX, posY), Vector2(sizeX, sizeY), text, font, borderColor, color)

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
        font.draw(batch, text, position.x + 20f, position.y + size.y / 2, size.x - 40, Align.center, true)
    }
}
