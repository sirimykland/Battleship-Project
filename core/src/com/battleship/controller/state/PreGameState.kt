package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.badlogic.gdx.math.Vector2
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.Game
import com.battleship.controller.input.TreasureHandler
import com.battleship.model.Player
import com.battleship.model.treasures.Treasure
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardRectangle
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.size
import com.battleship.utility.Palette
import com.battleship.view.PlayView
import com.battleship.view.View

class PreGameState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()

    val inputProcessor = TreasureHandler(GSM.activeGame!!.me.board)

    override fun create() {
        super.create()
        println("---PREGAMESTATE---")
        GSM.activeGame!!.me.board.createAndPlaceTreasures(1, Treasure.TreasureType.GOLDKEY, true)
        GSM.activeGame!!.me.board.createAndPlaceTreasures(1, Treasure.TreasureType.GOLDCOIN, true)
        GSM.activeGame!!.me.board.createAndPlaceTreasures(1, Treasure.TreasureType.TREASURECHEST, true)
        controller.addGameListener(GSM.activeGame!!.gameId, GSM.activeGame!!.me.playerId)
        addInputProcessor(inputProcessor)
    }

    private val readyButton = GUI.textButton(
            5f,
            3f,
            90f,
            10f,
            "Start Game",
            onClick = {
                val game = GSM.activeGame!!
                println("gameready is: " + game.gameReady)
                println("t: "+game.me.board.getTreasuresList())
                controller.registerTreasures(
                        game.gameId,
                        game.me.playerId,
                        game.me.board.getTreasuresList()
                )
                GSM.set(LoadingGameState(controller))
            })

    override val guiObjects: List<GuiObject> = listOf(
        readyButton,
        GUI.header("Place treasures"),
        GUI.backButton { GSM.set(MainMenuState(controller)) }
    )

    override fun render() {
        this.view.render(*guiObjects.toTypedArray(), GSM.activeGame!!.me.board)
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
