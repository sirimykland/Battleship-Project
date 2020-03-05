package com.battleship.model.ui

import com.badlogic.gdx.math.Vector2

class BlankButton(position: Vector2, size: Vector2, onClick: () -> Unit) :
    Button(position, size, onClick) {

    constructor(posx: Float, posy: Float, sizex: Float, sizey: Float, onClick: () -> Unit) :
        this(Vector2(posx, posy), Vector2(sizex, sizey), onClick)
}
