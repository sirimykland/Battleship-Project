package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject
import com.battleship.utility.Palette

abstract class MenuItem(val position: Vector2, val size: Vector2) : GameObject() {
    private val borderWidth = 10
    override fun draw(batch: SpriteBatch) {
        batch.draw(Palette.RED, position.x, position.y, size.x, size.y)
        batch.draw(Palette.BLACK,
            position.x + borderWidth / 2, position.y + borderWidth / 2,
            size.x - borderWidth, size.y - borderWidth)
    }
}
