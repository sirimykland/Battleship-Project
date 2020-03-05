package com.battleship.view

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.battleship.model.GameObject

abstract class View {

    protected var cam: OrthographicCamera

    init{
        cam = OrthographicCamera()
    }

    var batch: SpriteBatch = SpriteBatch()

    abstract fun render(sb: SpriteBatch)
}
