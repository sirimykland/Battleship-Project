package com.battleship.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.battleship.model.GameObject

abstract class View {

    var batch: SpriteBatch = SpriteBatch()


    abstract fun render(vararg gameObjects: GameObject)

}
