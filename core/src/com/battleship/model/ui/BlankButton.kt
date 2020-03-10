package com.battleship.model.ui

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.Palette

class BlankButton(
    position: Vector2,
    size: Vector2,
    borderColor: TextureRegion = Palette.ORANGE,
    color: TextureRegion = Palette.LIGHT_GREY,
    onClick: () -> Unit
) :
    Button(position, size, borderColor, color, onClick) {

    constructor(
        posX: Float,
        posY: Float,
        sizeX: Float,
        sizeY: Float,
        borderColor: TextureRegion = Palette.ORANGE,
        color: TextureRegion = Palette.LIGHT_GREY,
        onClick: () -> Unit
    ) :
        this(Vector2(posX, posY), Vector2(sizeX, sizeY), borderColor, color, onClick)
}
