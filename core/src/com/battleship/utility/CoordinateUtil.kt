package com.battleship.utility

import com.badlogic.gdx.math.Vector2

/**
 * Vector2 utility
 */
object CoordinateUtil {

    /**
     * Method that extends Vector2 class, and coverts a this vectors x/y values into a board coordinate
     *
     * @param boardPos: Vector2 - The board's pixel position
     * @param boardWidth: Float - The board's pixel width
     * @param boardSize: Int - The grid size of the board
     * @return Vector2 - coordinate on the board
     */
    fun Vector2.toCoordinate(boardPos: Vector2, boardWidth: Float, boardSize: Int): Vector2 {
        val tileSize = boardWidth / boardSize
        val tileX = (this.x - boardPos.x) / tileSize
        val tileY = (this.y - boardPos.y) / tileSize
        return Vector2(kotlin.math.floor(tileY), kotlin.math.floor(tileX))
    }
}
