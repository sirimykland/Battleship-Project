package com.battleship.controller.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.battleship.model.treasures.Treasure
import com.battleship.model.Board
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth

/**
 * Inherits behavior from [InputAdapter]
 * Class used to handle the moving of treasures in PreGameState
 * @constructor
 * @property board: Board - The board to be handled
 */
class TreasureHandler(private val board: Board) : InputAdapter() {
    private var activeTreasure: Treasure? = null
    private var oldPosition: Vector2? = null

    /**
     * Called when the screen was touched or a mouse button was pressed.
     * Makes the treasure touched the activeTreasure and stores it original position to oldPosition.
     * @param screenX - The x coordinate, origin is in the upper left corner
     * @param screenY - The y coordinate, origin is in the upper left corner
     * @param pointer - the pointer for the event.
     * @param button - the button
     * @return whether the input was processed
     */
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val touchPos = Vector2(screenX.toFloat(), Gdx.graphics.height - screenY.toFloat())
        val boardWidth = Gdx.graphics.boardWidth()
        val boardPos = Gdx.graphics.boardPosition()

        val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, board.size)
        val treasurePos = Vector2(boardTouchPos.y, boardTouchPos.x) // Flip position

        // Checks if touching a treasure. Sets it as the active treasure to be moved.
        val treasure = board.getTreasureByPosition(treasurePos)
        if (treasure != null) {
            activeTreasure = treasure
            activeTreasure!!.playSound()
            oldPosition = activeTreasure!!.position
        }
        return false
    }

    /**
     * Called when a finger was lifted or a mouse button was released.
     * Updates the activesTreasures position if it is valid. If not move it back to oldPosition
     * @param x - The x coordinate, origin is in the upper left corner
     * @param y - The y coordinate, origin is in the upper left corner
     * @param pointer - the pointer for the event.
     * @param button - the button
     * @return whether the input was processed
     */
    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        if (activeTreasure != null) {
            if (activeTreasure!!.position == oldPosition) {
                activeTreasure!!.rotate()
            }

            // Check if new position is validated. Goes back to old position if not.
            if (!board.validateTreasurePosition(activeTreasure)) {
                activeTreasure!!.updatePosition(oldPosition!!)
            }
        }
        activeTreasure = null
        oldPosition = null
        return false
    }

    /**
     * Called when a finger or the mouse was dragged.
     * Updates the activesTreasures position.
     * @param x - The x coordinate, origin is in the upper left corner
     * @param y - The y coordinate, origin is in the upper left corner
     * @param pointer - the pointer for the event.
     * @return whether the input was processed
     */
    override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean {
        if (activeTreasure != null) {
            val boardWidth = Gdx.graphics.boardWidth()
            val boardPos = Gdx.graphics.boardPosition()
            val newBoardTouchPos =
                Vector2(x.toFloat(), Gdx.graphics.height - y.toFloat()).toCoordinate(
                    boardPos,
                    boardWidth,
                    board.size
                )
            val newTreasurePos = Vector2(newBoardTouchPos.y, newBoardTouchPos.x)

            // Check if newTreasurePos is inside of the board
            if (newTreasurePos.x + activeTreasure!!.dimension.x <= board.size && newTreasurePos.x >= 0) {
                if (newTreasurePos.y + activeTreasure!!.dimension.y <= board.size && newTreasurePos.y >= 0) {
                    activeTreasure!!.updatePosition(newTreasurePos)
                }
            }
        }
        return false
    }
}
