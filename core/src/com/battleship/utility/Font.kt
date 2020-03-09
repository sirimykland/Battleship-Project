package com.battleship.utility

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
/*
 * Denne klassen vil laste inn alle fonter inn i minnet, så hvis vi vil optimalisere kan det lages
 * en løsning som ikke laster inn før de brukes.
 */
object Font {
    // White
    val LARGE_WHITE = BitmapFont(Gdx.files.internal("large_white.fnt")) // 40px
    val MEDIUM_WHITE = BitmapFont(Gdx.files.internal("medium_white.fnt")) // 28px
    val SMALL_WHITE = BitmapFont(Gdx.files.internal("small_white.fnt")) // 20px
    val TINY_WHITE = BitmapFont(Gdx.files.internal("tiny_white.fnt")) // 12px
    // Black
    val LARGE_BLACK = BitmapFont(Gdx.files.internal("large_black.fnt")) // 40px
    val MEDIUM_BLACK = BitmapFont(Gdx.files.internal("medium_black.fnt")) // 28px
    val SMALL_BLACK = BitmapFont(Gdx.files.internal("small_black.fnt")) // 20px
    val TINY_BLACK = BitmapFont(Gdx.files.internal("tiny_black.fnt")) // 12px

    fun dispose() {
        listOf(LARGE_BLACK, LARGE_WHITE, MEDIUM_WHITE, MEDIUM_BLACK,
            SMALL_WHITE, SMALL_BLACK, TINY_BLACK, TINY_WHITE).forEach {
            it.dispose()
        }
    }
}
