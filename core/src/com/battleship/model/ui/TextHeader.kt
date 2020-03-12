package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font
import com.battleship.utility.Palette

class TextHeader(
    position: Vector2,
    size: Vector2,
    val text: String,
    val font: BitmapFont = Font.MEDIUM_WHITE,
    color: TextureRegion = Palette.GREY
) :
    Header(position, size, color) {

    constructor(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        text: String,
        font: BitmapFont = Font.MEDIUM_WHITE,
        color: TextureRegion = Palette.GREY
    ) :
        this(Vector2(posX, posY), Vector2(sizeX, sizeY), text, font, color)

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
        font.draw(batch, text, position.x + 20f, position.y + size.y / 2, size.x - 40, Align.center, true)
    }
}
