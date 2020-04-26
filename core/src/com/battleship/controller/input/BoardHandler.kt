package com.battleship.controller.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth

/**
 * Inherits behavior from [InputAdapter]
 * Class used to handle making moves in PlayState.
 *
 * @constructor
 * @property controller: FirebaseController - Used to register the move in firebase
 * @property isGameOver - Used to check if game is over.
 */
class BoardHandler(private val controller: FirebaseController, val isGameOver: () -> Boolean) : InputAdapter() {

    /**
     * Called when the screen was touched or a mouse button was pressed.
     * Makes move is it is the players turn, opponents board is showing and if the game is not over.
     * @param screenX - The x coordinate, origin is in the upper left corner
     * @param screenY - The y coordinate, origin is in the upper left corner
     * @param pointer - the pointer for the event.
     * @param button - the button
     * @return whether the input was processed
     */
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (GSM.activeGame!!.isPlayersTurn() && !GSM.activeGame!!.playerBoard && !isGameOver()) {
            val touchPos = Vector2(screenX.toFloat(), Gdx.graphics.height - screenY.toFloat())

            val boardWidth = Gdx.graphics.boardWidth()
            val boardPos = Gdx.graphics.boardPosition()
            val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)

            if (boardBounds.contains(touchPos)) {
                val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, 10)
                controller.registerMove(
                    GSM.activeGame!!.gameId,
                    boardTouchPos.x.toInt(),
                    boardTouchPos.y.toInt(),
                    GSM.activeGame!!.player.playerId,
                    GSM.activeGame!!.player.equipmentSet.activeEquipment!!.name
                )
                GSM.activeGame!!.makeMove(boardTouchPos)
            }
        }
        return false
    }
}
