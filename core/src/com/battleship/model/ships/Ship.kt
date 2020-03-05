package com.battleship.model.ships

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

abstract class Ship(val position: Vector2) : GameObject() {
    abstract var dimension: Vector2
    abstract var name: String
    abstract var health: Int
    var shapeRenderer: ShapeRenderer = ShapeRenderer()
    var padding: Int = 1

    // TODO reimplement

    fun hit(coordinates: Vector2): Boolean {
        /*
        val rect = Rectangle(position.x, position.y, position.x + dimension.x - 1, position.y + dimension.y - 1)
        println(coordinates)
        println(rect)
        if (rect.contains(coordinates.x, coordinates.y)){
            return true
        }
        return false
        */
        println("Touch: " + coordinates)
        for (i in 1 until dimension.x.toInt() + 1) {
            val x = position.x + i - 1
            for (j in 1 until dimension.y.toInt() + 1) {
                val y = position.y + j - 1

                println("Ship: (" + x + "," + y + ")")
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

    // abstract fun hit(coordinates: Vector2): Boolean
    // abstract fun sunk(): Boolean
    // abstract fun takeDamage(damage: Int)
}
