package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * State handling all logic related to the game over screen
 */
class GameOverState(private val controller: FirebaseController, win: Boolean) : GuiState(controller) {
    private val menuList = listOf(
        Pair("Main Menu") { GameStateManager.set(MainMenuState(controller)) },
        Pair("Play Again") { GameStateManager.set(PreGameState(controller)) }
    )
    var winString = ""
        init {
            if (win) {
                winString = "You won the game!"
            } else {
                winString = "You lost the game..."
            }
    }
    override val guiObjects: List<GuiObject> = listOf(
        GUI.menuButton(
            25f,
            32f,
            "Back to main menu",
            onClick = { GameStateManager.set(MainMenuState(controller)) }
        ),
        GUI.menuButton(
            25f,
            54f,
            "Play again",
            onClick = { GameStateManager.set(MatchmakingState(controller)) }
        ),
        GUI.header(
            winString
        )
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}
