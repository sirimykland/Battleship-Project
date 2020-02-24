package com.battleship.model

import com.badlogic.gdx.math.Vector2

abstract class GameObject {
    var position:Vector2

    init {
        this.position = Vector2()
    }

    abstract fun draw()
}