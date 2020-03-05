package com.battleship.utility

import com.badlogic.gdx.Graphics
import com.badlogic.gdx.math.Vector2

object GdxGraphicsUtil {
    /*
     * this function extends Gdx.graphics
     */
    fun Graphics.boardPosition(): Vector2 {
        return Vector2(
                this.width.toFloat() * 0.05f,
                this.height / 2f - this.boardWidth() / 2f
        )
    }

    /*
     * this function extends Gdx.graphics
     */
    fun Graphics.boardWidth(): Float {
        return this.width.toFloat() * 0.9f
    }
}
