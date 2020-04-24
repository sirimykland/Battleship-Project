package com.battleship.view

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.battleship.model.GameObject
import com.battleship.utility.TextureLibrary

abstract class View {

    protected var batch: SpriteBatch = SpriteBatch()
    protected var shapeRenderer = ShapeRenderer()

    protected val background = Sprite(TextureLibrary.BACKGROUND)

    abstract fun render(vararg gameObjects: GameObject)

    open fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }
}
