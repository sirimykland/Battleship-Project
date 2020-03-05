package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.battleship.utility.Font

class TextButton(position: Vector2, size: Vector2, onClick: () -> Unit, val text: String) :
    Button(position, size, onClick) {

    constructor(posx: Float, posy: Float, sizex: Float, sizey: Float, onClick: () -> Unit, text: String) :
        this(Vector2(posx, posy), Vector2(sizex, sizey), onClick, text)

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
        Font.BASIC.draw(batch, text, position.x + 20f, position.y + size.y / 2, size.x - 40, Align.center, true)
    }
}
