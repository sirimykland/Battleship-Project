package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
// TODO Karl
abstract class GuiElement {
    abstract fun draw(batch: SpriteBatch, position: Vector2, size: Vector2)
    abstract val zIndex: Int
}
