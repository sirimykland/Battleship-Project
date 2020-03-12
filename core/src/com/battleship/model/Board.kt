package com.battleship.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.battleship.model.ships.MediumShip
import com.battleship.model.ships.Ship
import com.battleship.model.ships.SmallShip
import com.battleship.model.weapons.RadarWeapon
import com.battleship.model.weapons.Weapon

class Board(val size: Int) : GameObject() {
    var ships: ArrayList<Ship> = ArrayList()
    var board = Array(size) { Array(size) { Tile.UNGUESSED } }
    val tileRenderer: ShapeRenderer = ShapeRenderer()
    var padding: Int = 1

    fun addSmallShip(x: Int, y: Int) {
        // TODO add check
        val ship: SmallShip = SmallShip(Vector2(x.toFloat(), y.toFloat()))
        ships.add(ship)
    }

    fun addMediumShip(x: Int, y: Int) {
        // TODO add check
        val ship: MediumShip = MediumShip(Vector2(x.toFloat(), y.toFloat()))
        ships.add(ship)
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
            // println(ship.name + ": " + ship.hit(Vector2(220f, 240f)))
        }
    }

    fun shootTiles(boardTouchPos: Vector2, weapon: Weapon) {
        var x = boardTouchPos.x.toInt()
        var y = boardTouchPos.y.toInt()
        // Loops through the weapons radius
        for (x in x - weapon.radius until x + weapon.radius + 1 step 1) {
            for (y in y - weapon.radius until y + weapon.radius + 1 step 1) {
                // Checks if inside board
                if (x >= 0 && x < size && y >= 0 && y < size) {
                    updateTile(Vector2(x.toFloat(), y.toFloat()), weapon)
                }
            }
        }
    }

    fun updateTile(pos: Vector2, weapon: Weapon): Boolean {
        var shipPos = Vector2(pos.y, pos.x)

        if (board[pos.x.toInt()][pos.y.toInt()] == Tile.MISS || board[pos.x.toInt()][pos.y.toInt()] == Tile.HIT) {
            println("Guessed, pick new")
            return false
        }

        var hit = Tile.MISS
        for (ship in ships) {
            if (ship.hit(shipPos)) {
                hit = Tile.HIT
                ship.takeDamage(weapon.damage)
            }
        }

        // TODO implement
        if (weapon is RadarWeapon) {
            hit = Tile.NEAR
        }

        println(hit)
        board[pos.x.toInt()][pos.y.toInt()] = hit
        return hit == Tile.HIT
    }

    enum class Tile {
        HIT, MISS, UNGUESSED, NEAR
    }
}
