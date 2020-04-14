package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.Player
import com.battleship.model.treasures.Treasure
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardRectangle
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.size
import com.battleship.view.PlayView
import com.battleship.view.View

class PreGameState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    private val boardSize = 10
    var player: Player = Player(boardSize)

    override fun create() {
        super.create()
        player.board.createAndPlaceTreasures(1, Treasure.TreasureType.TREASURECHEST, true)
        player.board.createAndPlaceTreasures(2, Treasure.TreasureType.GOLDCOIN, true)
        player.board.createAndPlaceTreasures(2, Treasure.TreasureType.GOLDKEY, true)
    }

    private val readyButton = GUI.textButton(
        5f,
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
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
            val touchPos = Vector2(touchX, touchY)

            val screenSize = Gdx.graphics.size()

            // Check if input is on the board
            if (Gdx.graphics.boardRectangle().contains(touchPos)) {
                val boardPos = Gdx.graphics.boardPosition()
                val boardWidth = Gdx.graphics.boardWidth()
            }
        }
    }
}
