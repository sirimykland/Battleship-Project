package com.battleship.utility

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

object Palette {
    private val texture = Texture("colors/palette.png")
    val WHITE = TextureRegion(texture, 0, 0, 1, 1)
    val BLACK = TextureRegion(texture, 1, 0, 1, 1)
    val DARK_GREY = TextureRegion(texture, 2, 0, 1, 1)
    val LIGHT_GREY = TextureRegion(texture, 3, 0, 1, 1)
    val RED = TextureRegion(texture, 0, 1, 1, 1)
    val BLUE = TextureRegion(texture, 1, 1, 1, 1)
    val GREEN = TextureRegion(texture, 2, 1, 1, 1)
    val YELLOW = TextureRegion(texture, 3, 1, 1, 1)
    val GREY_TRANSPARENT = TextureRegion(texture, 0, 2, 1, 1)
    val LIGHT_BLUE = TextureRegion(texture, 1, 2, 1, 1)
    val ORANGE = TextureRegion(texture, 2, 2, 1, 1)
    val DARK_GREEN = TextureRegion(texture, 3, 2, 1, 1)
    val PINK = TextureRegion(texture, 0, 3, 1, 1)
    val VIOLET = TextureRegion(texture, 1, 3, 1, 1)
    val BURGUNDY = TextureRegion(texture, 2, 3, 1, 1)
    val DARK_PURPLE = TextureRegion(texture, 3, 3, 1, 1)

    /**
     *  Disposes texture when the Application is destroyed.
     */
    fun dispose() {
        texture.dispose()
    }
}
