package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.battleship.model.GameObject

class MainMenuView() : View() {

    override fun render(vararg gameObjects: GameObject) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        gameObjects.forEach {
            it.draw(batch)
        }
        batch.end()
    }
}
