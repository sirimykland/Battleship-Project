package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.battleship.GameStateManager
import com.battleship.model.Player
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.GUI
import com.battleship.utility.GdxGraphicsUtil.boardPosition
import com.battleship.utility.GdxGraphicsUtil.boardRectangle
import com.battleship.utility.GdxGraphicsUtil.boardWidth
import com.battleship.utility.GdxGraphicsUtil.size
import com.battleship.utility.GdxGraphicsUtil.weaponsetPosition
import com.battleship.utility.GdxGraphicsUtil.weaponsetSize
import com.battleship.utility.Palette
import com.battleship.view.PlayView
import com.battleship.view.View

class PreGameState : GuiState() {
    override var view: View = PlayView()
    val boardSize = 10
    var player: Player = Player(boardSize)

    override fun create() {
        super.create()
        player.board.createAndPlaceTreasurechests(4, true)
        player.board.createAndPlaceGoldcoins(2, true)
    }

    private val readyButton = GuiObject(Gdx.graphics.weaponsetPosition(),
        Gdx.graphics.weaponsetSize())
        .with(Background(Palette.BLACK))
        .with(Border(Palette.WHITE, 10f))
        .with(Text("Start Game"))
        .onClick {
            println("Player are ready")
            // GameStateManager.gameController.registerShip(player.board.getships()) - dette må lages
            GameStateManager.set(PlayState())
        }

    override val guiObjects: List<GuiObject> = listOf(
        readyButton, GUI.header("Place ships"), GUI.backButton { GameStateManager.set(MainMenuState()) }
    )

    override fun render() {
        this.view.render(*guiObjects.toTypedArray(), player.board)
    }

    override fun update(dt: Float) {
        handleInput()
    }

    fun handleInput() {
        // drag ship / set position relative to global
        if (Gdx.input.justTouched()) {
            val touchPos =
                Vector2(
                    Gdx.input.x.toFloat(),
                    Gdx.graphics.height - Gdx.input.y.toFloat())
            val screenSize = Gdx.graphics.size()
            // if input on board
            if (Gdx.graphics.boardRectangle().contains(touchPos)) {
                // if input on ship
                // println("   on board")
                val boardPos = Gdx.graphics.boardPosition()
                val boardWidth = Gdx.graphics.boardWidth()
                // TODO
                // select ship
                // set new midletidig position
                // if released, snap to board
            }
        }
    }
}
