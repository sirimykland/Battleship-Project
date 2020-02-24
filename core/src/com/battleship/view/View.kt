package com.battleship.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class View {

    protected var batch: SpriteBatch

    init {
        batch = SpriteBatch()
    }

    internal abstract fun render()

}