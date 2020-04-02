package com.battleship.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.battleship.model.equipment.Equipment
import com.battleship.model.equipment.MetalDetector
import com.battleship.model.treasures.GoldCoin
import com.battleship.model.treasures.Treasure
import com.battleship.model.treasures.TreasureChest
import kotlin.random.Random

class Board(val size: Int) : GameObject() {
    private var treasures: ArrayList<Treasure> = ArrayList()
    private var board = Array(size) { Array(size) { Tile.UNGUESSED } }
    private val tileRenderer: ShapeRenderer = ShapeRenderer()
    var padding: Int = 1

    // TODO: Refactor. Use createAndPlaceTreasures instead
    fun createAndPlaceTreasurechests(treasureNumber: Int, revealed: Boolean) {
        var treasure: Treasure
        for (i in 0 until treasureNumber) {
            do {
                treasure = TreasureChest(
                    Vector2(
                        Random.nextInt(
                            0,
                            size
                        ).toFloat(), Random.nextInt(0, size).toFloat()
                    ), Random.nextBoolean()
                )
            } while (!validateTreasurePosition(treasure))
            treasure.revealed = revealed
            treasures.add(treasure)
        }
    }

    // TODO: Refactor. Use createAndPlaceTreasures instead
    fun createAndPlaceGoldcoins(treasureNumber: Int, revealed: Boolean) {
        var treasure: Treasure
        for (i in 0 until treasureNumber) {
            do {
                treasure = GoldCoin(
                    Vector2(
                        Random.nextInt(
                            0,
                            size
                        ).toFloat(), Random.nextInt(0, size).toFloat()
                    ), Random.nextBoolean()
                )
            } while (!validateTreasurePosition(treasure))
            treasure.revealed = revealed
            treasures.add(treasure)
        }
    }

    // TODO: Implement usage of this method
    fun createAndPlaceTreasures(treasures: ArrayList<Treasure>) {
        for (treasure in treasures) {
            do {
                treasure.position.set(
                    Random.nextInt(0, size).toFloat(),
                    Random.nextInt(0, size).toFloat()
                )
            } while (!validateTreasurePosition(treasure))
            treasures.add(treasure)
        }
    }

    private fun validateTreasurePosition(treasure: Treasure): Boolean {
        for (tile in treasure.getTreasureTiles()) {
            // TODO: Insert explanation
            if (tile.x >= size || tile.y >= size) return false

            // TODO: Insert explanation
            for (placedShip in treasures) {
                if (placedShip.getTreasureTiles().contains(tile)) {
                    return false
                }
            }
        }
        return true
    }

    override fun draw(batch: SpriteBatch, position: Vector2, dimension: Vector2) {
        var x = position.x
        var y = position.y
        val tileSize = dimension.x / size

        // Draw board
        for (array in board) {
            for (value in array) {

                tileRenderer.begin(ShapeRenderer.ShapeType.Filled)
                when (value) {
                    Tile.HIT -> tileRenderer.color = Color.GREEN
                    Tile.UNGUESSED -> tileRenderer.color = Color.BLUE
                    Tile.MISS -> tileRenderer.color = Color.RED
                    Tile.NEAR -> tileRenderer.color = Color.YELLOW
                }

                tileRenderer.rect(x, y, tileSize, tileSize)
                tileRenderer.end()
                x += tileSize + padding
            }
            y += tileSize + padding
            x = position.x
        }

        // Draw treasures
        for (treasure in treasures) {
            treasure.draw(batch, position, Vector2(tileSize, tileSize))
        }
    }

    fun shootTiles(boardTouchPos: Vector2, equipment: Equipment) {
        equipment.use()
        val xSearchMin = boardTouchPos.x.toInt() - equipment.searchRadius
        val xSearchMax = boardTouchPos.x.toInt() + equipment.searchRadius + 1
        val ySearchMin = boardTouchPos.y.toInt() - equipment.searchRadius
        val ySearchMax = boardTouchPos.y.toInt() + equipment.searchRadius + 1

        // Loops through the equipments search radius
        for (x in xSearchMin until xSearchMax) {
            for (y in ySearchMin until ySearchMax) {
                // Check if inside board
                if (x in 0 until size && y in 0 until size) {
                    updateTile(Vector2(x.toFloat(), y.toFloat()), equipment)
                }
            }
        }
    }

    private fun updateTile(pos: Vector2, equipment: Equipment) {
        var shipPos = Vector2(pos.y, pos.x) // Flip position

        // Check if tile is already opened
        if (getTile(pos) == Tile.MISS || getTile(pos) == Tile.HIT) {
            println("Already guessed, pick a new tile")
            return
        }

        var hit = Tile.MISS
        var treasureHit = getTreasureByPosition(shipPos)

        if (treasureHit != null) {
            println("Hit!")
            hit = Tile.HIT
            treasureHit.takeDamage()
            if (treasureHit.found()) {
                println(treasureHit.name + " found!")
            }
        } else {
            println("Miss!")
        }

        // TODO: Implement feature
        if (equipment is MetalDetector) {
            hit = Tile.NEAR
        }

        board[pos.x.toInt()][pos.y.toInt()] = hit
    }

    private fun getTile(pos: Vector2): Board.Tile {
        return board[pos.x.toInt()][pos.y.toInt()]
    }

    private fun getTreasureByPosition(pos: Vector2): Treasure? {
        for (ship in treasures) {
            if (ship.hit(pos)) {
                return ship
            }
        }
        return null
    }

    fun getAllTreasureHealth(): Int {
        var health = 0
        for (ship in treasures) {
            health += ship.health
        }
        return health
    }

    enum class Tile {
        HIT, MISS, UNGUESSED, NEAR
    }
}
