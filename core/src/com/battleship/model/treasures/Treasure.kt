package com.battleship.model.treasures

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

abstract class Treasure(var position: Vector2) : GameObject() {
    abstract var dimension: Vector2
    abstract var name: String
    abstract var health: Int
    abstract var sprite: Sprite
    var padding = 1
    var revealed = false

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

    fun takeDamage() {
        health--
    }

    fun found(): Boolean {
        return health == 0
    }

    fun rotateTreasure() {
        val temp = dimension.x
        dimension.x = dimension.y
        dimension.y = temp
    }

    override fun draw(batch: SpriteBatch, boardPos: Vector2, dimension: Vector2) {
        if (found() || revealed) {

            val newX = boardPos.x + dimension.x * position.x + position.x * padding
            val newY = boardPos.y + dimension.y * position.y + position.y * padding

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
    }

    fun getTreasureTiles(): ArrayList<Vector2> {
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
