package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.Palette

class Background(val color: TextureRegion = Palette.BLACK) : GuiElement() {
    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        batch.draw(color, position.x, position.y, size.x, size.y)
    }

    override val zIndex: Int = -2
}
