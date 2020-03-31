package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.controller.firebase.GameController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.gameInfoPosition
import com.battleship.utility.GdxGraphicsUtil.gameInfoSize
import com.battleship.view.PlayView
import com.battleship.view.View

class PlayState() : GuiState() {
    override var view: View = PlayView()
    var boardSize = 10

    private val gameController = GameController()
    private val testText = GUI.text(
            Gdx.graphics.gameInfoPosition().x,
            Gdx.graphics.gameInfoPosition().y,
            Gdx.graphics.gameInfoSize().x,
            Gdx.graphics.gameInfoSize().y,
            "Find treasures"
    )
    override val guiObjects: List<GuiObject> = listOf(
            testText
    )

    override fun create() {
        super.create()
        gameController.addGameListener(GSM.activeGame.gameId)
    }

    private fun headerText(): String {
        if (GSM.activeGame.opponent.playerId == "") return "Waiting for opponent..."
        else if (GSM.activeGame.isMyTurn()) return (GSM.activeGame.opponent.playerName + "'s Board")
        return "Waiting for opponents turn"
    }

    // override val guiObjects: List<GuiObject> = listOf(   )

    override fun render() {
        this.view.render(GUI.header(headerText()),
                *guiObjects.toTypedArray(),
                GSM.activeGame.opponent.board,
                GSM.activeGame.me.equipmentSet)
    }

    override fun update(dt: Float) {
        if (GSM.activeGame.isMyTurn()) {
            handleInput()
            GSM.activeGame.opponent.updateHealth()
            if (GSM.activeGame.opponent.health == 0) {
                println("You won!")
                gameController.setWinner(GSM.userId, GSM.activeGame.gameId)
                GSM.set(MainMenuState())
            }
        }
    }

    private fun handleInput() {
        if (Gdx.input.justTouched()) {
            val touchPos =
                    Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())
            val boardWidth = Gdx.graphics.boardWidth()
            val boardPos = Gdx.graphics.boardPosition()
            val boardBounds = Rectangle(boardPos.x, boardPos.y, boardWidth, boardWidth)
            if (boardBounds.contains(touchPos)) {
                val boardTouchPos = touchPos.toCoordinate(boardPos, boardWidth, boardSize)
                val game = GSM.activeGame
                if (game.isMyTurn()) {
                    // println("IsMyTurn:"+ true)
                    if (GSM.activeGame.makeMove(boardTouchPos)) {
                        gameController.makeMove(
                                game.gameId,
                                boardTouchPos.x,
                                boardTouchPos.y,
                                game.me.playerId,
                                game.me.equipmentSet.activeEquipment!!.name)
                    }
                }
            }
        }
    }

    override fun dispose() {
        super.dispose()
        gameController.detachGameListener(GSM.activeGame.gameId)
    }
}