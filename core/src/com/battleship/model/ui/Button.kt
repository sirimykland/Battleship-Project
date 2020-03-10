package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.controller.input.ButtonHandler

abstract class Button(
    position: Vector2,
    size: Vector2,
    borderColor: TextureRegion,
    color: TextureRegion,
    onClick: () -> Unit
) :
    MenuItem(position, size, borderColor, color) {
    val listener = ButtonHandler(position, size, onClick)
}
