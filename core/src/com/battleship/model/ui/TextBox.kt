package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font

class TextBox(position: Vector2, size: Vector2, val text: String) : MenuItem(position, size) {

    constructor(posX: Float, posY: Float, sizeX: Float, sizeY: Float, text: String) :
        this(Vector2(posX, posY), Vector2(sizeX, sizeY), text)

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
        Font.BASIC.draw(batch, text, position.x + 20f, position.y + size.y / 2, size.x - 40, Align.center, true)
    }
}
