package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * State handling all logic related to the game over screen
 */
class GameOverState : GuiState() {
    private val menuList = listOf(
        Pair("Main Menu") { GameStateManager.set(MainMenuState()) },
        Pair("Play Again") { GameStateManager.set(PreGameState()) }
    )

    override val guiObjects: List<GuiObject> = listOf(
        GUI.menuButton(
            Gdx.graphics.width / 2 - 170f,
            Gdx.graphics.height - 600f,
            "Back to main menu",
            onClick = { GameStateManager.set(MainMenuState()) }
        ),
        GUI.menuButton(
            Gdx.graphics.width / 2 - 170f,
            Gdx.graphics.height - 450f,
            "Play again",
            onClick = { GameStateManager.set(MatchmakingState()) }
        ),
        GUI.header(
            "Game over"
        )
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}