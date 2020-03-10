package com.battleship.model.ui

import com.badlogic.gdx.math.Vector2

class BlankButton(position: Vector2, size: Vector2, onClick: () -> Unit) :
    Button(position, size, onClick) {

    constructor(posX: Float, posY: Float, sizeX: Float, sizeY: Float, onClick: () -> Unit) :
        this(Vector2(posX, posY), Vector2(sizeX, sizeY), onClick)
}
