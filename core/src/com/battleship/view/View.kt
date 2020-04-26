package com.battleship.view

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.battleship.model.GameObject
import com.battleship.utility.TextureLibrary

/**
 * Abstract View class
 */
abstract class View {

    protected var batch: SpriteBatch = SpriteBatch()
    protected var shapeRenderer = ShapeRenderer()

    protected val background = Sprite(TextureLibrary.BACKGROUND)

    /**
     * Abstract render function for rendering game and gui related
     * objects to the screen.
     *
     * @param gameObjects: GameObject - variable length of drawable
     * objects to render
     */
    abstract fun render(vararg gameObjects: GameObject)

    /**
     * Disposes of batch and ShapeRenderer.
     */
    open fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }
}
