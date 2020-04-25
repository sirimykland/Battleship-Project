package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.battleship.model.GameObject
import com.battleship.utility.TextureLibrary

abstract class View {

    protected val background = Sprite(TextureLibrary.BACKGROUND)
    protected val backgroundScale = maxOf(
        Gdx.graphics.width / background.width,
        Gdx.graphics.height / background.height,
        1f
    )

    var batch: SpriteBatch = SpriteBatch()
    var shapeRenderer = ShapeRenderer()

    abstract fun render(vararg gameObjects: GameObject)

    open fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }
}
