package com.battleship.utility

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

object TextureLibrary {
    val BACKGROUND = TextureRegion(Texture("images/background.png"))

    val TREASURECHEST = TextureRegion(Texture("images/treasures/chest.png"))
    val GOLDCOIN = TextureRegion(Texture("images/treasures/coin.png"))
    val GOLDKEY = TextureRegion(Texture("images/treasures/key.png"))
    val GOLDKEY_ROTATED = TextureRegion(Texture("images/treasures/rotatedKey.png"))

    /**
     *  Disposes textures when the Application is destroyed.
     */
    fun dispose() {
        listOf(BACKGROUND, TREASURECHEST, GOLDCOIN, GOLDKEY, GOLDKEY_ROTATED)
            .map {
                it.texture
            }
            .forEach {
                it.dispose()
            }
    }
}
