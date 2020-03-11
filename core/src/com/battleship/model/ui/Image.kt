package com.battleship.model.ui
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

class Image(val position: Vector2, val texturePath: String) : GameObject() {

    constructor(posX: Float, posY: Float, texturePath: String) :
        this(Vector2(posX, posY), texturePath)

    private val texture: Texture = Texture(texturePath)

    override fun draw(batch: SpriteBatch) {
        batch.draw(texture, position.x, position.y)
    }
}
