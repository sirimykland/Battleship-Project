package com.battleship.model.ui.deprecated

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

@Deprecated("Use GuiObject instead")
abstract class Header(position: Vector2, size: Vector2, color: TextureRegion) : MenuItem(position, size, color, color)
