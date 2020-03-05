package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.battleship.model.GameObject
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth

class PlayView() : View() {
    /*
     *  uses com.battleship.utility.GdxGraphicsUtil.*
     */
    override fun render(gameObject: GameObject) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameObject.draw(batch,
                Gdx.graphics.boardPosition(), Gdx.graphics.boardWidth())
    }
}
