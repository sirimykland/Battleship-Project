package com.battleship.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.battleship.model.ships.MediumShip
import com.battleship.model.ships.Ship
import com.battleship.model.ships.SmallShip
import kotlin.random.Random

class Board(val size: Int) : GameObject() {
    var ships: ArrayList<Ship> = ArrayList()
    var board = Array(size) { Array(size) { Tile.UNGUESSED } }
    val tileRenderer: ShapeRenderer = ShapeRenderer()
    var padding: Int = 1

    fun addSmallShip(x: Int, y: Int) {
        // TODO add check
        val ship: SmallShip = SmallShip(Vector2(x.toFloat(), y.toFloat()), false)
        ships.add(ship)
    }

    fun addMediumShip(x: Int, y: Int) {
        // TODO add check
        val ship: MediumShip = MediumShip(Vector2(x.toFloat(), y.toFloat()), false)
        ships.add(ship)
    }

    /*
     * creates ships and places them
     */
    fun randomPlacement(shipNumber: Int) {
        var ship: Ship
        for (i in 0..shipNumber) {
            do {
                ship = MediumShip(Vector2(Random.nextInt(0, size).toFloat(), Random.nextInt(0, size).toFloat()), Random.nextBoolean())
                println("shipposition: (" + ship.position.x + ", " + ship.position.y + ")")
            } while (!validateShipPosition(ship))
            ships.add(ship)
        }
    }

    /*
     * This for placing a defined list of ships
     */
    fun randomPlacement(ships: ArrayList<Ship>) {
        for (ship in ships) {
            do {
                ship.position.set(Random.nextInt(0, size).toFloat(), Random.nextInt(0, size).toFloat())
                //println("ship position: (" + ship.position.x + ", " + ship.position.y + ")")
            } while (!validateShipPosition(ship))
            ships.add(ship)
        }
    }

    fun validateShipPosition(ship: Ship): Boolean {
        for (tile in ship.getTiles()) {
            if (tile.x >= size || tile.y >= size) return false
            for (placedShip in ships) {
                if (placedShip.getTiles().contains(tile)) {
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

        for (ship in ships) {
            ship.draw(batch, position, Vector2(tileSize, tileSize))
        }
    }

    fun updateTile(pos: Vector2): Boolean {
        val shipPos = Vector2(pos.y, pos.x)

        if (board[pos.x.toInt()][pos.y.toInt()] == Tile.MISS || board[pos.x.toInt()][pos.y.toInt()] == Tile.HIT) {
            println("Guessed, pick new")
            return false
        }

        var hit = Tile.MISS
        for (ship in ships) {
            if (ship.hit(shipPos)) {
                hit = Tile.HIT
                ship.takeDamage(1)
            }
        }

        println(hit)
        board[pos.x.toInt()][pos.y.toInt()] = hit
        return hit == Tile.HIT
    }

    enum class Tile {
        HIT, MISS, UNGUESSED, NEAR
    }
}