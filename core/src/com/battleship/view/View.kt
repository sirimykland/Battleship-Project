package com.battleship.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.battleship.model.GameObject

abstract class View {

    var batch: SpriteBatch = SpriteBatch()
    var shapeRenderer = ShapeRenderer()

    abstract fun render(vararg gameObjects: GameObject)

    open fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }
}
