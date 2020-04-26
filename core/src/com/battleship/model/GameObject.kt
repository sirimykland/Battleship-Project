package com.battleship.model

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2

/**
 * Abstract class used within drawable models
 */
abstract class GameObject {

    /**
     * Open function draw that draws graphics
     *
     * @param batch SpriteBatch - to draw text and images with
     */
    open fun draw(batch: SpriteBatch) {}

    /**
     * Open function draw that draws graphics
     *
     * @param batch SpriteBatch - to draw text and images with
     * @param position Vector2 - the position to start drawing
     * @param dimension Vector2 - the size of the object to draw
     */
    open fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {}

    /**
     * Open function draw that draws graphics
     *
     * @param batch Spritebatch - to draw text and images with
     * @param shapeRenderer ShapeRenderer - to draw shapes with
     * @param position Vector2 - the position to start drawing
     * @param dimension Vector2 - the size of the object to draw
     */
    open fun draw(batch: SpriteBatch, shapeRenderer: ShapeRenderer, position: Vector2, dimension: Vector2) {}
}
