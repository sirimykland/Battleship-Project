package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.battleship.model.GameObject

class BasicView : View() {
    var backgroundTexture: Texture = Texture("images/background3.jpg")
    var backgroundSprite: Sprite = Sprite(backgroundTexture)

    override fun render(vararg gameObjects: GameObject) {
        Gdx.gl.glClearColor(50f, 15f, 55f, 19f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        backgroundSprite.setCenter((Gdx.graphics.getWidth()/2).toFloat(), (Gdx.graphics.getHeight()/2).toFloat());
        backgroundSprite.draw(batch)
        gameObjects.forEach {
            it.draw(batch)
        }
        batch.end()
    }
}
