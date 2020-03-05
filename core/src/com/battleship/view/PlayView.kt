package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject
import com.battleship.model.ui.TextBox

class PlayView() : View() {
    /*
     *  uses com.battleship.utility.GdxGraphicsUtil.*
     */
    override fun render(gameObject: GameObject) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        TextBox(Vector2(100f, 100f), Vector2(500f, 100f), "Hello World").draw(batch)
        batch.end()
    }
}
