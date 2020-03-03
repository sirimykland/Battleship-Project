package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

class PlayView() : View() {

    override fun render(gameObject: GameObject) {
        var height = Gdx.graphics.height
        var boardWith = Gdx.graphics.width.toFloat()*0.9f
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameObject.draw(batch,
            Vector2(Gdx.graphics.width.toFloat() * 0.05f, height / 2f - boardWith / 2f), boardWith)
    }
}
