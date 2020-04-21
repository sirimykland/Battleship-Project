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

class BoardHandler(private val controller: FirebaseController) : InputAdapter() {
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (GSM.activeGame!!.isPlayersTurn() && !GSM.activeGame!!.playerBoard) {
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