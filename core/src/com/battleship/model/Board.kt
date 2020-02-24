package com.battleship.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Board :GameObject(){
    var img:Texture = Texture("badlogic.jpg")
    override fun draw(batch:SpriteBatch) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()
    }

}