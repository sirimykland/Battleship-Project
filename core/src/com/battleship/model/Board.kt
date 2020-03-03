package com.battleship.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.model.ships.MediumShip
import com.battleship.model.ships.Ship
import com.battleship.model.ships.SmallShip

class Board(size: Int) : GameObject() {
    var ships : ArrayList<Ship> = ArrayList()
    var board = Array(size) {Array(size) {Tile.UNGUESSED} }
    val tileRenderer: ShapeRenderer = ShapeRenderer()
    var tileSize: Int = 20
    var padding: Int = 1
    var size = size

    fun addSmallShip(x: Int, y: Int){
        // TODO add check
        val ship : SmallShip =  SmallShip(Vector2(x.toFloat(), y.toFloat()))
        ships.add(ship)
    }

    fun addMediumShip(x: Int, y: Int){
        // TODO add check
        val ship : MediumShip = MediumShip(Vector2(x.toFloat(), y.toFloat()))
        ships.add(ship)
    }


    override fun draw(batch: SpriteBatch, position: Vector2, boardWidth: Float) {
        var x = position.x
        var y = position.y
        var tileSize = boardWidth/size
        for (array in board) {
            for (value in array) {
                tileRenderer.begin(ShapeRenderer.ShapeType.Filled)

                when(value){
                    Tile.HIT -> tileRenderer.setColor(Color.GREEN)
                    Tile.UNGUESSED -> tileRenderer.setColor(Color.BLUE)
                    Tile.MISS -> tileRenderer.setColor(Color.RED)
                    Tile.NEAR -> tileRenderer.setColor(Color.YELLOW)
                }

                tileRenderer.rect(x, y, tileSize, tileSize)
                tileRenderer.end()
                x += tileSize + padding
            }
            y += tileSize + padding
            x = position.x
        }

        batch.begin()
        //batch.draw(img, position.x, position.y)
        batch.end()
        for (ship in ships){
            ship.draw(batch, position, tileSize)
            //println(ship.name + ": " + ship.hit(Vector2(220f, 240f)))
        }

    }
    fun updateTile(pos: Vector2){
        board[pos.x.toInt()][pos.y.toInt()] = Tile.HIT
    }

    /*
    fun updateTile(pos: Vector2) {
        //Check if hit
        var hit = Tile.MISS
        var hittedShip: Ship? = null
        for (ship in ships){
            if(ship.hit(pos)){
                hit = Tile.HIT
                hittedShip = ship
            }
        }

        var x = position.x
        var y = position.y

        // Iterate and update right tile
        for(i in 0 until board.size){
            for(j in 0 until board[i].size){
                val rect = Rectangle(x, y, tileSize.toFloat(), tileSize.toFloat())
                if(rect.contains(pos)){
                    if(board[i][j] != Tile.HIT && board[i][j] != Tile.MISS){
                        board[i][j] = hit
                        hittedShip?.takeDamage(1)

                    }else{
                        println("Already guessed, pick another tile")
                        return
                    }

                }
                x += tileSize + padding
            }

            y += tileSize + padding
            x = position.x
        }
        println(hit)
        if(hittedShip != null){
            if(hittedShip!!.sunk()){
                println(hittedShip.name + " Sunk")
                ships.remove(hittedShip)

            }
        }

    }

     */

    enum class Tile{
        HIT, MISS, UNGUESSED, NEAR
    }

}
