package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.battleship.model.GameObject

/**
 * Subclass of [View] that render gui objects to the screen
 */
class BasicView : View() {

    /**
     * Renders gui related objects to the screen such as text, buttons and images.
     * GuiObjects are drawn using their draw method.
     *
     * @param gameObjects: GameObject - variable length of drawable
     * objects to render
     */
    override fun render(vararg gameObjects: GameObject) {
        Gdx.gl.glClearColor(50f, 15f, 55f, 19f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        background.setCenter(Gdx.graphics.width / 1.85f, Gdx.graphics.height.toFloat() / 2)
        background.draw(batch)
        gameObjects.forEach { it.draw(batch) }
        batch.end()
    }
}
