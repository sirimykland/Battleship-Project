package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.battleship.model.GameObject

class SettingsView() : View() {

    override fun render(gameObject: GameObject) {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameObject.draw(batch)
    }
}
