package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font
import com.battleship.utility.Palette

open class TextButton(
    position: Vector2,
    size: Vector2,
    var text: String,
    private val font: BitmapFont = Font.SMALL_WHITE,
    borderColor: TextureRegion = Palette.RED,
    color: TextureRegion = Palette.BLACK,
    onClick: () -> Unit
) :
    Button(position, size, borderColor, color, onClick) {

    constructor(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        text: String,
        font: BitmapFont = Font.SMALL_WHITE,
        borderColor: TextureRegion = Palette.RED,
        color: TextureRegion = Palette.BLACK,
        onClick: () -> Unit
    ) :
        this(Vector2(posX, posY), Vector2(sizeX, sizeY), text, font, borderColor, color, onClick)

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
        font.draw(batch, text, position.x + 20f, position.y + size.y / 2, size.x - 40, Align.center, true)
    }
}
