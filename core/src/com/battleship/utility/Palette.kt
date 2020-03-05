package com.battleship.utility

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

object Palette {
    private val texture = Texture("palette.png")
    val WHITE = TextureRegion(texture, 0, 0, 1, 1)
    val BLACK = TextureRegion(texture, 1, 0, 1, 1)
    val RED = TextureRegion(texture, 0, 1, 1, 1)
}
