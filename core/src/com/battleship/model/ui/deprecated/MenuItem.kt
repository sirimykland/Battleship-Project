package com.battleship.model.ui.deprecated

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

@Deprecated("Use GuiObject instead")
abstract class MenuItem(
    val position: Vector2,
    val size: Vector2,
    private val borderColor: TextureRegion,
    private val color: TextureRegion
) :
    GameObject() {
    private val borderWidth = 10
    override fun draw(batch: SpriteBatch) {
        batch.draw(borderColor, position.x, position.y, size.x, size.y)
        batch.draw(color,
            position.x + borderWidth / 2, position.y + borderWidth / 2,
            size.x - borderWidth, size.y - borderWidth)
    }
}
