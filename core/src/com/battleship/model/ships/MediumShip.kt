package com.battleship.model.ships

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

class MediumShip(position: Vector2) : Ship() {
    override var dimension: Vector2 = Vector2(1f,3f)
    override var name: String = "MediumShip"
    override var health: Int = 3
    override var position: Vector2 = position
    var shapeRenderer: ShapeRenderer = ShapeRenderer()

    override fun draw(batch: SpriteBatch, boardPos: Vector2, tileSize: Float) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.setColor(Color.WHITE)
        // TODO draw relative to board
        var newX = boardPos.x + tileSize*position.x
        var newY = boardPos.y + tileSize*position.y
        shapeRenderer.rect(newX, newY, dimension.x*tileSize, dimension.y*tileSize)
        shapeRenderer.end()
    }


}