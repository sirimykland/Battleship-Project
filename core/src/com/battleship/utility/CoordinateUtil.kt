package com.battleship.utility

import com.badlogic.gdx.math.Vector2

// object extensions, import _methods_ as needed
object CoordinateUtil {
    /*
     * this function extends Vector2
     */
    fun Vector2.toCoordinate(boardPos: Vector2, boardWidth: Float, boardSize: Int): Vector2 {
        var tileSize = boardWidth / boardSize
        var tileX = (this.x - boardPos.x) / tileSize
        var tileY = (this.y - boardPos.y) / tileSize
        return Vector2(kotlin.math.floor(tileY), kotlin.math.floor(tileX))
    }
}