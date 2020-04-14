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
import kotlin.random.Random

class Board(val size: Int) : GameObject() {
    private var treasures: ArrayList<Treasure> = ArrayList()
    private var tiles = Array(size) { Array(size) { Tile.PREGAME } }

    // Change all tiles to unopened state
    fun setTilesUnopened() {
        tiles = Array(size) { Array(size) { Tile.UNOPENED } }
    }

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

    fun validateTreasurePosition(treasure: Treasure?): Boolean {
        if (treasure == null) {
            return false
        }

        for (tile in treasure.getTreasureTiles()) {
            // Tile outside board
            if (tile.x >= size || tile.y >= size || tile.x < 0 || tile.y < 0) {
                return false
            }

            // Another tile already in this place
            for (placedShip in treasures) {
                if (placedShip.getTreasureTiles().contains(tile) && placedShip != treasure) {
                    return false
                }
            }
        }
        return true
    }

    override fun draw(batch: SpriteBatch, shapeRenderer: ShapeRenderer, position: Vector2, dimension: Vector2) {
        var x = position.x
        var y = position.y
        val tileSize = dimension.x / size

        // Draw board
        for (row in tiles) {
            for (value in row) {
                if (value == Tile.PREGAME) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                    Gdx.gl.glLineWidth(3f)
                    shapeRenderer.color = Color.DARK_GRAY
                    shapeRenderer.rect(x, y, tileSize, tileSize)
                    shapeRenderer.end()
                } else {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                    when (value) {
                        Tile.HIT -> shapeRenderer.color = Color.GREEN
                        Tile.MISS -> shapeRenderer.color = Color.RED
                        Tile.NEAR -> shapeRenderer.color = Color.YELLOW
                        Tile.UNOPENED -> shapeRenderer.color = Color.BLACK
                    }
                    shapeRenderer.rect(x, y, tileSize, tileSize)
                    shapeRenderer.end()

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                    shapeRenderer.color = Color.WHITE
                    shapeRenderer.rect(x, y, tileSize, tileSize)
                    shapeRenderer.end()
                }

                x += tileSize
            }
            y += tileSize
            x = position.x
        }

        // Draw treasures
        for (treasure in treasures) {
            treasure.draw(batch, position, Vector2(tileSize, tileSize))
        }
    }

    fun shootTiles(boardTouchPos: Vector2, equipment: Equipment): ArrayList<Result> {
        val xSearchMin = boardTouchPos.x.toInt() - equipment.searchRadius
        val xSearchMax = boardTouchPos.x.toInt() + equipment.searchRadius + 1
        val ySearchMin = boardTouchPos.y.toInt() - equipment.searchRadius
        val ySearchMax = boardTouchPos.y.toInt() + equipment.searchRadius + 1

        // Loops through the tiles in the equipments search radius and adds the result when explored to the list
        var resultList = ArrayList<Result>()
        for (x in xSearchMin until xSearchMax) {
            for (y in ySearchMin until ySearchMax) {
                // Check if inside board
                if (x in 0 until size && y in 0 until size) {
                    resultList.add(exploreTile(Vector2(x.toFloat(), y.toFloat())))
                }
            }
        }
        // Returns a list of the results of all tiles explored
        return resultList
    }

    private fun exploreTile(pos: Vector2): Result {
        val treasurePos = Vector2(pos.y, pos.x) // Flip position
        val treasure = getTreasureByPosition(treasurePos)
        val boardTile = getTile(pos)

        // Checks first if tile is previously explored. Checks so if tile contains a treasure and returns result
        return if (boardTile == Tile.MISS || boardTile == Tile.HIT) {
            Result.NOT_VALID
        } else if (treasure != null) {
            setTile(pos, Tile.HIT)
            treasure.takeDamage()
            if (treasure.found()) {
                treasure.playSound(0.8f)
                Result.FOUND
            } else {
                Result.HIT
            }
        } else {
            setTile(pos, Tile.MISS)
            Result.MISS
        }
    }

    private fun getTile(pos: Vector2): Tile {
        return tiles[pos.x.toInt()][pos.y.toInt()]
    }

    private fun setTile(pos: Vector2, tile: Tile) {
        tiles[pos.x.toInt()][pos.y.toInt()] = tile
    }

    fun getTreasureByPosition(pos: Vector2): Treasure? {
        for (treasure in treasures) {
            if (treasure.hit(pos)) {
                return treasure
            }
        }
        return null
    }

    fun getCombinedTreasureHealth(): Int {
        var health = 0
        for (treasure in treasures) {
            health += treasure.health
        }
        return health
    }

    enum class Tile {
        HIT, MISS, NEAR, UNOPENED, PREGAME
    }

    enum class Result {
        HIT, FOUND, MISS, NOT_VALID
    }
}
