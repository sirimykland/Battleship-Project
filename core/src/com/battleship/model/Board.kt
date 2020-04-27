package com.battleship.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.battleship.model.equipment.Equipment
import com.battleship.model.treasures.GoldCoin
import com.battleship.model.treasures.GoldKey
import com.battleship.model.treasures.Treasure
import com.battleship.model.treasures.Treasure.TreasureType
import com.battleship.model.treasures.TreasureChest
import com.battleship.utility.SoundEffects
import kotlin.random.Random

/**
 * Board class inheriting from [GameObject].
 *
 * @property size: Int - grid size of the board
 */
class Board(val size: Int) : GameObject() {
    private var treasures: ArrayList<Treasure> = ArrayList()
    private var tiles = Array(size) { Array(size) { Tile.PREGAME } }

    /**
     * Removes all the elements in the treasure list
     */
    fun clearTreasures() {
        treasures.clear()
    }

    /**
     * Creates treasures a number of treasures of a certain type, that can be revealed or not.
     * Treasures are placed onto the board at a random position.
     *
     * @param quantity: Int - number of treasures to instantiate
     * @param type: TreasureType - enum type of treasure to instantiate
     * @param revealed: Boolean - true or false value of whether treasures should be revealed or not.
     */
    fun createAndPlaceTreasures(quantity: Int, type: TreasureType, revealed: Boolean) {
        var treasure: Treasure
        for (i in 0 until quantity) {
            do {
                val x = Random.nextInt(0, size).toFloat()
                val y = Random.nextInt(0, size).toFloat()

                treasure = when (type) {
                    TreasureType.TREASURECHEST -> TreasureChest(Vector2(x, y), Random.nextBoolean())
                    TreasureType.GOLDCOIN -> GoldCoin(Vector2(x, y), Random.nextBoolean())
                    TreasureType.GOLDKEY -> GoldKey(Vector2(x, y), Random.nextBoolean())
                }
            } while (!validateTreasurePosition(treasure))

            treasure.revealed = revealed
            treasures.add(treasure)
        }
    }

    /**
     * Validates if treasure has a valid board position
     *
     * @param treasure: Treasure - treasure whose position are to be validated
     * @return Boolean of whether position is valid or not
     */
    fun validateTreasurePosition(treasure: Treasure?): Boolean {
        if (treasure == null) {
            return false
        }

        for (tile in treasure.getTreasureTiles()) {
            if (tile.x >= size || tile.y >= size || tile.x < 0 || tile.y < 0) {
                return false
            }

            for (placedShip in treasures) {
                if (placedShip.getTreasureTiles().contains(tile) && placedShip != treasure) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Override method of [GameObject].
     * draws the board grid, and calls for treasures to be drawn
     * onto the board.
     *
     * @param batch SpriteBatch - to draw text and images with
     * @param shapeRenderer: ShapeRenderer - to draw shapes with
     * @param position Vector2 - the position to start drawing
     * @param dimension Vector2 - the size of the object to draw
     */
    override fun draw(
        batch: SpriteBatch,
        shapeRenderer: ShapeRenderer,
        position: Vector2,
        dimension: Vector2
    ) {
        var x = position.x
        var y = position.y
        val tileSize = dimension.x / size

        for (row in tiles) {
            for (value in row) {
                if (value == Tile.PREGAME) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                    Gdx.gl.glLineWidth(5f)
                    shapeRenderer.color = Color.DARK_GRAY
                    shapeRenderer.rect(x, y, tileSize, tileSize)
                    shapeRenderer.end()
                } else {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                    when (value) {
                        Tile.HIT -> shapeRenderer.color =
                            Color(0.302f, 0.816f, 0.546f, 1f)
                        Tile.MISS -> shapeRenderer.color =
                            Color(0.961f, 0.298f, 0.298f, 1f)
                        Tile.NEAR -> shapeRenderer.color =
                            Color(0.950f, 0.961f, 0.298f, 1f)
                        Tile.UNOPENED -> shapeRenderer.color =
                            Color(0.905f, 0.882f, 0.612f, 1f)
                    }
                    shapeRenderer.rect(x, y, tileSize, tileSize)
                    shapeRenderer.end()

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                    shapeRenderer.color = Color.DARK_GRAY
                    shapeRenderer.rect(x, y, tileSize, tileSize)
                    shapeRenderer.end()
                }
                x += tileSize
            }
            y += tileSize
            x = position.x
        }

        for (treasure in treasures) {
            treasure.draw(batch, position, Vector2(tileSize, tileSize))
        }
    }

    /**
     * Sets all treasures' revealed flag to true.
     */
    fun revealTreasures() {
        treasures.forEach { treasure -> treasure.revealed = true }
    }

    /**
     * Searches the position + the equipments radius on the board for treasures
     *
     * @param boardTouchPos Vector2 - The position relative to the board of the move
     * @param equipment: ShapeRenderer - the equipment used
     * @return Boolean. For if it should switch turn to the other player
     */
    fun shootTiles(boardTouchPos: Vector2, equipment: Equipment): Boolean {
        val xSearchMin = boardTouchPos.x.toInt() - equipment.searchRadius
        val xSearchMax = boardTouchPos.x.toInt() + equipment.searchRadius + 1
        val ySearchMin = boardTouchPos.y.toInt() - equipment.searchRadius
        val ySearchMax = boardTouchPos.y.toInt() + equipment.searchRadius + 1

        val resultList = ArrayList<Result>()
        for (x in xSearchMin until xSearchMax) {
            for (y in ySearchMin until ySearchMax) {
                if (x in 0 until size && y in 0 until size) {
                    resultList.add(exploreTile(Vector2(x.toFloat(), y.toFloat())))
                }
            }
        }
        return when {
            resultList.contains(Result.FOUND) -> {
                println("Found")
                equipment.use()
                false
            }
            resultList.contains(Result.HIT) -> {
                println("Hit")
                SoundEffects.playHit()
                equipment.use()
                false
            }
            resultList.all { n -> n == Result.NOT_VALID } -> {
                println("Not valid, try again")
                false
            }
            else -> {
                println("Missed")
                equipment.playSound()
                equipment.use()
                true
            }
        }
    }

    /**
     * Explores the tile of the given position on the board.
     *
     * @param pos Vector2 - The position relative to the board of the move
     * @return Result
     */
    private fun exploreTile(pos: Vector2): Result {
        val treasurePos = Vector2(pos.y, pos.x)
        val treasure = getTreasureByPosition(treasurePos)
        val boardTile = getTile(pos)

        return if (boardTile == Tile.MISS || boardTile == Tile.HIT) {
            Result.NOT_VALID
        } else if (treasure != null) {
            setTile(pos, Tile.HIT)
            treasure.takeDamage()
            if (treasure.found()) {
                treasure.playSound()
                Result.FOUND
            } else {
                Result.HIT
            }
        } else {
            setTile(pos, Tile.MISS)
            Result.MISS
        }
    }

    /**
     * Gets the Tile of a given position
     *
     * @param pos Vector2 - The position relative to the board
     * @return Tile of the given position
     */
    private fun getTile(pos: Vector2): Tile {
        return tiles[pos.x.toInt()][pos.y.toInt()]
    }

    /**
     * Updates the Tile of a given position
     *
     * @param pos Vector2 - The position relative to the board
     * @param tile Vector2 - The tile to be updated to
     */
    private fun setTile(pos: Vector2, tile: Tile) {
        tiles[pos.x.toInt()][pos.y.toInt()] = tile
    }

    /**
     * Gets treasure at a position on the board
     *
     * @param pos: Vector2 - board coordinate
     * @return Treasure - treasure at that position
     */
    fun getTreasureByPosition(pos: Vector2): Treasure? {
        for (treasure in treasures) {
            if (treasure.hit(pos)) {
                return treasure
            }
        }
        return null
    }

    /**
     * Adds all treasures' health
     *
     * @return Int - total health of all treasures
     */
    fun getCombinedTreasureHealth(): Int {
        var health = 0
        for (treasure in treasures) {
            health += treasure.health
        }
        return health
    }

    /**
     * Checks if treasure list is empty
     *
     * @return Boolean
     */
    fun isTreasureListEmpty(): Boolean {
        return treasures.isEmpty()
    }

    /**
     * Converts ArrayList of treasures to list of map
     *
     * @return treasuresList List<Map<String, Any>>
     */
    fun getTreasuresList(): List<Map<String, Any>> {
        val treasuresList = ArrayList<Map<String, Any>>()
        for (treasure in treasures) {
            treasuresList.add(treasure.toMap())
        }
        return treasuresList
    }

    /**
     * Sets treasures list from list with map
     *
     * @param treasuresList List<Map<String, Any>>
     */
    fun setTreasuresList(treasuresList: List<Map<String, Any>>) {
        val newTreasures = ArrayList<Treasure>()

        for (treasure in treasuresList) {
            val position = Vector2(
                (treasure["x"] as Number).toFloat(),
                (treasure["y"] as Number).toFloat()
            )
            val rotate =
                if (treasure.containsKey("rotate")) {
                    treasure["rotate"] as Boolean
                } else false
            val type = treasure["type"] as String
            val newTreasure = when (TreasureType.valueOf(type)) {
                TreasureType.GOLDCOIN -> {
                    GoldCoin(position, rotate)
                }
                TreasureType.GOLDKEY -> {
                    GoldKey(position, rotate)
                }
                TreasureType.TREASURECHEST -> {
                    TreasureChest(position, rotate)
                }
            }
            newTreasures.add(newTreasure)
        }
        treasures = newTreasures
    }

    /**
     * toString specific for Board
     * @return String
     */
    override fun toString(): String {
        return "Board(treasure=$treasures)"
    }

    /**
     * Types of Tile states
     */
    enum class Tile {
        HIT, MISS, NEAR, UNOPENED, PREGAME
    }

    /**
     * Types of Result
     */
    enum class Result {
        HIT, FOUND, MISS, NOT_VALID
    }
}
