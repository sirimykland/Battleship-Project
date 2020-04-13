package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.GameController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * State handling all logic related to the main menu
 */
class MainMenuState(private val controller : FirebaseController) : GuiState(controller) {

    private val menuList = listOf(
            Pair("Create game as Olivia") {
                GSM.userId = "zmWpyb8luZAMrBwzY97x"
                val gameId = gameController.createGame(GSM.userId)
                if (gameId.isNotEmpty()) gameController.setGame(gameId)
                GSM.set(PreGameState(controller))
            },
            Pair("Settings") { GSM.set(SettingsState(controller)) },
            Pair("Matchmaking") { GSM.set(MatchmakingState(controller)) }
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
        11f,
        74f,
        78f,
        12.5f,
        "Treasure hunt",
        font = Font.XXL_BLACK

    )
    private val skeleton: GuiObject = GUI.image(
        35f,
        60f,
        14f,
        14f,
        "images/skeleton.png"
    )
    private val map: GuiObject = GUI.image(
        52f,
        59f,
        16f,
        16f,
        "images/skull.png"
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(title, skeleton, map, *guiObjects.toTypedArray())
    }
}
