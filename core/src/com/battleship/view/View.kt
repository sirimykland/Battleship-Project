package com.battleship.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class View {

    var batch: SpriteBatch = SpriteBatch()


    abstract fun render(vararg gameObjects: GameObject)

}
