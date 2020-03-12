package com.battleship.model.weapons

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

abstract class Weapon : GameObject() {
    abstract var damage: Int
    abstract var radius: Int
    abstract var ammunition: Int
    abstract var name: String
    abstract var active: Boolean
    var shapeRenderer: ShapeRenderer = ShapeRenderer()

    // NOT IN USE
    override fun draw(batch: SpriteBatch, drawPos: Vector2, dimension: Vector2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        if (active) {
            shapeRenderer.color = Color.GOLD
        } else {
            shapeRenderer.color = Color.CYAN
        }
        shapeRenderer.rect(drawPos.x, drawPos.y, dimension.x, dimension.y)
        shapeRenderer.end()
    }

    fun hasAmmunition(): Boolean{
        return ammunition > 0
    }

    fun shoot(){
        ammunition--
    }
}
