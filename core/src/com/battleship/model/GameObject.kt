package com.battleship.model

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

abstract class GameObject {
    open fun draw(batch: SpriteBatch) {}
    open fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {}
    open fun draw(batch: SpriteBatch, shapeRenderer: ShapeRenderer, position: Vector2, dimension: Vector2) {}
}
