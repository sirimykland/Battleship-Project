package com.battleship.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class GameInfo(var player: Player) : GameObject() {
    var shapeRenderer = ShapeRenderer()

    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.LIGHT_GRAY
        shapeRenderer.rect(position.x, position.y, dimension.x, dimension.y)
        shapeRenderer.end()
    }
}