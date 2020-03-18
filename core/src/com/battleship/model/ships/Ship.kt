package com.battleship.model.ships

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

abstract class Ship(var position: Vector2) : GameObject() {
    abstract var dimension: Vector2
    abstract var name: String
    abstract var health: Int
    abstract var sprite: Sprite
    var shapeRenderer: ShapeRenderer = ShapeRenderer()
    var padding: Int = 1

    fun hit(coordinates: Vector2): Boolean {
        for (i in 1 until dimension.x.toInt() + 1) {
            val x = position.x + i - 1
            for (j in 1 until dimension.y.toInt() + 1) {
                val y = position.y + j - 1

                // println("Ship: (" + x + "," + y + ")")
                if (coordinates.epsilonEquals(x, y)) {
                    return true
                }
            }
        }

        return false
    }

    fun takeDamage(damage: Int) {
        health -= damage
    }

    fun sunk(): Boolean {
        return health == 0
    }

    fun rotateShip() {
        val temp = dimension.x
        dimension.x = dimension.y
        dimension.y = temp
    }

    override fun draw(batch: SpriteBatch, boardPos: Vector2, dimension: Vector2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.WHITE

        val newX = boardPos.x + dimension.x * position.x + position.x * padding
        val newY = boardPos.y + dimension.y * position.y + position.y * padding
        shapeRenderer.rect(
            newX,
            newY,
            this.dimension.x * dimension.x,
            this.dimension.y * dimension.y
        )
        shapeRenderer.end()
        batch.begin()
        batch.draw(
            sprite.texture,
            newX,
            newY,
            this.dimension.x * dimension.x,
            this.dimension.y * dimension.y
        )
        batch.end()
    }

    fun getTiles(): ArrayList<Vector2> {
        val tiles = ArrayList<Vector2>()
        for (i in 1 until dimension.x.toInt() + 1) {
            val x = position.x.toInt() + i - 1
            for (j in 1 until dimension.y.toInt() + 1) {
                val y = position.y.toInt() + j - 1
                tiles.add(Vector2(x.toFloat(), y.toFloat()))
            }
        }
        return tiles
    }
}
