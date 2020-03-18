package com.battleship.model.ui.deprecated

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.controller.input.ButtonHandler

@Deprecated("Use GuiObject instead")
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
