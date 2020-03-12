package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

abstract class Header(position: Vector2, size: Vector2, color: TextureRegion) : MenuItem(position, size, color, color)
