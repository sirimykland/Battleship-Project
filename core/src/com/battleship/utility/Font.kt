package com.battleship.utility

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont

/**
 * Singleton that loads fonts into memory
 */
object Font {
    val XXL_WHITE = BitmapFont(Gdx.files.internal("font/xxl_white.fnt"))
    val XL_WHITE = BitmapFont(Gdx.files.internal("font/xl_white.fnt"))
    val LARGE_WHITE = BitmapFont(Gdx.files.internal("font/large_white.fnt"))
    val MEDIUM_WHITE = BitmapFont(Gdx.files.internal("font/medium_white.fnt"))
    val SMALL_WHITE = BitmapFont(Gdx.files.internal("font/small_white.fnt"))
    val TINY_WHITE = BitmapFont(Gdx.files.internal("font/tiny_white.fnt"))
    val XXL_BLACK = BitmapFont(Gdx.files.internal("font/xxl_black.fnt"))
    val XL_BLACK = BitmapFont(Gdx.files.internal("font/xl_black.fnt"))
    val LARGE_BLACK = BitmapFont(Gdx.files.internal("font/large_black.fnt"))
    val MEDIUM_BLACK = BitmapFont(Gdx.files.internal("font/medium_black.fnt"))
    val SMALL_BLACK = BitmapFont(Gdx.files.internal("font/small_black.fnt"))
    val TINY_BLACK = BitmapFont(Gdx.files.internal("font/tiny_black.fnt"))

    /**
     *  Disposes all fonts when the Application is destroyed.
     */
    fun dispose() {
        listOf(XXL_WHITE, XL_WHITE, XXL_BLACK, XL_BLACK, LARGE_BLACK, LARGE_WHITE, MEDIUM_WHITE, MEDIUM_BLACK,
            SMALL_WHITE, SMALL_BLACK, TINY_BLACK, TINY_WHITE).forEach {
            it.dispose()
        }
    }
}
