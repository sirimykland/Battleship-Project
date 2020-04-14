package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * State handling all logic related to the main menu
 */
class MainMenuState(private val controller: FirebaseController) : GuiState(controller) {

    private val menuList = listOf(
        Pair("Play") { GameStateManager.set(PreGameState(controller)) },
        Pair("Settings") { GameStateManager.set(SettingsState(controller)) },
        Pair("Matchmaking") { GameStateManager.set(MatchmakingState(controller)) }

    )

    override val guiObjects: List<GuiObject> = menuList
        .mapIndexed { i, (name, func) ->
            GUI.menuButton(
                23.4375f,
                100f - (56.25f + 18.75f * i),
                name,
                onClick = func
            )
        }

    private val title: GuiObject = GUI.text(
        10f,
        72f,
        80f,
        12.5f,
        "Treasure hunt",
        font = Font.XXL_BLACK
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(title, *guiObjects.toTypedArray())
    }
}
