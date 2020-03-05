package com.battleship.model.ui

import com.badlogic.gdx.math.Vector2
import com.battleship.controller.ButtonHandler

abstract class Button(position: Vector2, size: Vector2, onClick: () -> Unit) : MenuItem(position, size) {
    val listener = ButtonHandler(position, size, onClick)
}
