package com.battleship.model.ui
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
/**
 * Inherits behavior from [GuiElement]
 *
 * @constructor
 * @param texturePath: String - Path to image that should be used
 */
class Image(texturePath: String) : GuiElement() {

    private val texture: Texture = Texture(texturePath)

    /**
     * Draws the image
     * @param batch: SpriteBatch - The SpriteBatch object used to draw the image
     * @param position: Vector2 - The position on the screen where the image should be drawn
     * @param size: Vector2 - The size of the image
     */
    override fun draw(batch: SpriteBatch, position: Vector2, size: Vector2) {
        batch.draw(texture, position.x, position.y, size.x, size.y)
    }

    override val zIndex: Int = 0
}
