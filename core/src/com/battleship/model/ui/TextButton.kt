package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font

open class TextButton(position: Vector2, size: Vector2, var text: String, var onClick: () -> Unit) :
    Button(position, size, onClick) {

    constructor(posX: Float, posY: Float, sizeX: Float, sizeY: Float, text: String, onClick: () -> Unit) :
        this(Vector2(posX, posY), Vector2(sizeX, sizeY), text, onClick)

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
        Font.BASIC.draw(batch, text, position.x + 20f, position.y + size.y / 2, size.x - 40, Align.center, true)
    }
}
