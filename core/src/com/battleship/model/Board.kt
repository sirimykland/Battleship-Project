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

class Board(val size: Int = 10) : GameObject() {
    private var treasures: ArrayList<Treasure> = ArrayList()
    private var board = Array(size) { Array(size) { Tile.UNGUESSED } }
    private val tileRenderer: ShapeRenderer = ShapeRenderer()
    var padding: Int = 1
    var boardColor : Color = Color.BLUE;

    /*
     * creates treasures and places them
     */
    fun createAndPlaceTreasurechests(treasureNumber: Int, reveiled: Boolean) {
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
            treasure.reveiled = reveiled
            treasures.add(treasure)
        }
    }

    fun createAndPlaceGoldcoins(treasureNumber: Int, reveiled: Boolean) {
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
            treasure.reveiled = reveiled
            treasures.add(treasure)
        }
    }

    fun moveTreasure() {
        println("test move treasure()")
    }

    /*
     * This for placing a defined list of ships
     */
    fun createAndPlaceTreasures(treasures: ArrayList<Treasure>) {
        for (treasure in treasures) {
            do {
                treasure.position.set(
                    Random.nextInt(0, size).toFloat(),
                    Random.nextInt(0, size).toFloat()
                )
                // println("ship position: (" + ship.position.x + ", " + ship.position.y + ")")
            } while (!validateTreasurePosition(treasure))
            treasures.add(treasure)
        }
    }

    fun validateTreasurePosition(treasure: Treasure): Boolean {
        for (tile in treasure.getTreasureTiles()) {
            if (tile.x >= size || tile.y >= size) return false
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
        for (array in board) {
            for (value in array) {
                tileRenderer.begin(ShapeRenderer.ShapeType.Filled)

                when (value) {
                    Tile.HIT -> tileRenderer.color = Color.GREEN
                    Tile.UNGUESSED -> tileRenderer.color = boardColor
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

        for (treasure in treasures) {
            treasure.draw(batch, position, Vector2(tileSize, tileSize))
        }
    }

    // her er det to sett med x og y variabler ?
    fun shootTiles(boardTouchPos: Vector2, equipment: Equipment) {
        equipment.use()
        val x = boardTouchPos.x.toInt()
        val y = boardTouchPos.y.toInt()
        // Loops through the equipents search radius
        for (x in x - equipment.searchRadius until x + equipment.searchRadius + 1 step 1) {
            for (y in y - equipment.searchRadius until y + equipment.searchRadius + 1 step 1) {
                // Checks if inside board
                if (x >= 0 && x < size && y >= 0 && y < size) {
                    updateTile(Vector2(x.toFloat(), y.toFloat()), equipment)
                }
            }
        }
    }

    // TODO needs cleanup
    fun updateTile(pos: Vector2, equipment: Equipment) {
        var shipPos = Vector2(pos.y, pos.x)

        val boardTile = getTile(pos)
        if (boardTile == Tile.MISS || boardTile == Tile.HIT) {
            println("Guessed, pick new")
            return
        }

        var hit = Tile.MISS
        var hittedTreasure = getTreasureByPosition(shipPos)
        if (hittedTreasure != null) {
            println("Hitted")
            hit = Tile.HIT
            hittedTreasure.takeDamage()
            if (hittedTreasure.found()) {
                println(hittedTreasure.name + " Found")
            }
        } else {
            println("Missed")
        }

        // TODO implement
        if (equipment is MetalDetector) {
            hit = Tile.NEAR
        }

        board[pos.x.toInt()][pos.y.toInt()] = hit
    }

    fun getTile(pos: Vector2): Board.Tile {
        return board[pos.x.toInt()][pos.y.toInt()]
    }

    fun getTreasureByPosition(pos: Vector2): Treasure? {
        for (ship in treasures) {
            if (ship.hit(pos)) {
                return ship
            }
        }
        return null
    }

    fun getAllTreasueHealth(): Int {
    /**
     * converts arraylist of ship to list of map
     * @return shipsList List<Map<String, Any>>
     */
    fun getShipList(): List<Map<String, Any>> {
        var shipsList = ArrayList<Map<String, Any>>()
        for (ship in ships) {
            shipsList.add(ship.toMap())
        }
        return shipsList
    }

    /**
     * sets ships arraylist from list with map
     * @param shipsList List<Map<String, Any>>
     */
    fun setShipList(shipsList: List<Map<String, Any>>) {
        ships = ArrayList<Ship>()
        lateinit var newShip: Ship
        lateinit var position: Vector2
        var rotate: Boolean = true
        for (ship in shipsList) {
            position = Vector2((ship["x"] as Number).toFloat(), (ship["y"] as Number).toFloat())
            rotate = if (ship.containsKey("rotate")) ship["rotate"] as Boolean else false
            when (ship["type"]) {
                "SmallShip" ->
                    newShip = SmallShip(position, rotate)
                "MediumShip" ->
                    newShip = MediumShip(position, rotate)
            }
            ships.add(newShip)
        }
    }

    fun getAllShipHealth(): Int {
        var health = 0
        for (ship in treasures) {
            health += ship.health
        }
        return health
    }

    override fun toString(): String {
        return "Board(size=$size, ships=$ships)"
    }

    enum class Tile {
        HIT, MISS, UNGUESSED, NEAR
    }
}
