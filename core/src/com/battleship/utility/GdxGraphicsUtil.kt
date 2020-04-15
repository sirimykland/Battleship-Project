package com.battleship.utility

import com.badlogic.gdx.Graphics
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

object GdxGraphicsUtil {
    fun Graphics.size(): Vector2 {
        return Vector2(
                this.width.toFloat(),
                this.height.toFloat()
        )
    }

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

    fun Graphics.boardRectangle(): Rectangle {
        val pos = this.boardPosition()
        return Rectangle(
                pos.x,
                pos.y,
                this.boardWidth(),
                this.boardWidth()
        )
    }

    fun Graphics.equipmentSetSize(): Vector2 {
        return Vector2(88f, 10f)
    }

    fun Graphics.equipmentSetPosition(): Vector2 {
        return Vector2(5f, 2f)
    }

    fun Graphics.gameInfoSize(): Vector2 {
        return Vector2(this.width.toFloat(), this.boardPosition().y / 2)
    }

    fun Graphics.gameInfoPosition(): Vector2 {
        return Vector2(
                0f,
                this.height - this.gameInfoSize().y
        )
    }
}
