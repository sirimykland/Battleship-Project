package com.battleship.model.treasures

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.battleship.model.GameObject

/**
 * Abstract class for treasures inheriting from [GameObject].
 *
 * @constructor:
 * @property position: Vector2 - position of treasure on board grid
 * @property rotate: Boolean - describes if treasure is rotated, default: false
 */
abstract class Treasure(var position: Vector2, private val rotate: Boolean = false) : GameObject() {
    abstract var dimension: Vector2
    abstract var name: String
    abstract var health: Int
    abstract var sprite: Sprite
    abstract var type: TreasureType
    abstract var sound: Sound
    private var padding = 3
    var revealed = false

    /**
     * Triggers a sound.
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playSound(volume: Float = 0.8f) {
        SoundEffects.play(sound, volume)
    }

    /**
     * Types of Treasures
     */
    enum class TreasureType {
        TREASURECHEST, GOLDCOIN, GOLDKEY
    }

    /**
     * Checks if a hit is valid
     *
     * @param coordinates: Vector2 - position on the grid
     * @return boolean of valid hit
     */
    fun hit(coordinates: Vector2): Boolean {
        for (i in 1 until dimension.x.toInt() + 1) {
            val x = position.x + i - 1
            for (j in 1 until dimension.y.toInt() + 1) {
                val y = position.y + j - 1

                if (coordinates.epsilonEquals(x, y)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Decrement a treasure's health by 1
     */
    fun takeDamage() {
        health--
    }

    /**
     * Wraps treasure into a map
     * Map contains treasuretype, position and rotation boolean
     * @return shipMap: Map<String, Any>
     */
    fun toMap(): Map<String, Any> {
        val shipMap = mutableMapOf<String, Any>()
        shipMap["type"] = type.toString()
        shipMap["x"] = position.x
        shipMap["y"] = position.y
        shipMap["rotate"] = rotate
        return shipMap
    }

    /**
     * @return boolean of whether treasure is found or not
     */
    fun found(): Boolean {
        return health == 0
    }

    /**
     * Sets new position of the treasure
     *
     * @param pos: Vector2 - new position
     */
    fun updatePosition(pos: Vector2) {
        position = pos
    }

    /**
     * Rotate treasure 90 degrees, by swapping dimention vector's x and y
     */
    fun rotateTreasure() {
        val temp = dimension.x
        dimension.x = dimension.y
        dimension.y = temp
    }

    /**
     * Override method of [GameObject].
     * draws treasure with texture, and position relative to provided position
     * of the board.
     *
     * @param batch SpriteBatch - to draw text and images with
     * @param position Vector2 - the position to start drawing
     * @param dimension Vector2 - the size of the object to draw
     */
    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        if (found() || revealed) {

            val xPos = position.x + dimension.x * this.position.x + padding
            val yPos = position.y + dimension.y * this.position.y + padding
            val width = this.dimension.x * dimension.x - (padding * 2)
            val height = this.dimension.y * dimension.y - (padding * 2)

            val spriteTexture = sprite.texture
            spriteTexture.magFilter
            spriteTexture.setFilter(spriteTexture.minFilter, spriteTexture.magFilter)

            batch.begin()
            batch.draw(sprite.texture, xPos, yPos, width, height)
            batch.end()
        }
    }

    /**
     * Gets the position of all the tiles that the treasure span.
     *
     * @return tiles: ArrayList<Vector2> - list of grid coordinates
     */
    fun getTreasureTiles(): ArrayList<Vector2> {
        val tiles = ArrayList<Vector2>()
        for (i in 1 until dimension.x.toInt() + 1) {
            val x = position.x.toInt() + i - 1
            for (j in 1 until dimension.y.toInt() + 1) {
                val y = position.y.toInt() + j - 1
                tiles.add(Vector2(x.toFloat(), y.toFloat()))
            }
        }
        return tiles
    }

    /**
     * toString specific for Treasures
     * @return String
     */
    override fun toString(): String {
        return "Treasure(pos=$position, rotate=$rotate, name='$name')"
    }
}
