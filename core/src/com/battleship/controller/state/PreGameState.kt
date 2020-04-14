package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.input.TreasureHandler
import com.battleship.model.Player
import com.battleship.model.treasures.Treasure
import com.battleship.model.ui.GuiObject
import com.battleship.utility.CoordinateUtil.toCoordinate
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.view.PlayView
import com.battleship.view.View

class PreGameState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    private val boardSize = 10
    var player: Player = Player(boardSize)
    val inputProcessor = TreasureHandler(player.board)

    override fun create() {
        super.create()
        player.board.createAndPlaceTreasures(2, Treasure.TreasureType.TREASURECHEST, true)
        player.board.createAndPlaceTreasures(2, Treasure.TreasureType.GOLDCOIN, true)
        player.board.createAndPlaceTreasures(2, Treasure.TreasureType.BOOT, true)


        Gdx.input.inputProcessor = inputProcessor
    }

    private val readyButton = GUI.textButton(
        6f,
        3f,
        90f,
        10f,
        "Start Game",
        onClick = {
            println("Player are ready")
            // GameStateManager.gameController.registerShip(player.board.getShips()) TODO: Create
            GameStateManager.set(PlayState(controller))
        }
    )

    override val guiObjects: List<GuiObject> = listOf(
        readyButton,
        GUI.header("Place treasures"),
        GUI.backButton { GameStateManager.set(MainMenuState(controller)) }
    )

    override fun render() {
        this.view.render(*guiObjects.toTypedArray(), player.board)
    }

    override fun update(dt: Float) {
        handleInput()
    }

    fun handleInput() {
        // Drag ship
        if (Gdx.input.isTouched) {


            /*
            if (active != null) {
                //println(treasure.name)
                val x2 = Gdx.input.deltaX + touchPos.x
                val y2 = Gdx.input.deltaY + touchPos.y

                val newBoardTouchPos = Vector2(x2, y2).toCoordinate(boardPos, boardWidth, boardSize)
                val newTreasurePos = Vector2(newBoardTouchPos.y, newBoardTouchPos.x)
                println(newTreasurePos)
                active!!.updatePosition(newTreasurePos)

            }
            */

        }
    }
}
