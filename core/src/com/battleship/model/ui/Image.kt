package com.battleship.model.ui
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class Image(texturePath: String) : GuiElement() {

    private val texture: Texture = Texture(texturePath)

    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        batch.draw(texture, position.x, position.y)
        // TODO: Size doesn't matter here. Find a solution to remove from constructor
    }

    override val zIndex: Int = 0
}
