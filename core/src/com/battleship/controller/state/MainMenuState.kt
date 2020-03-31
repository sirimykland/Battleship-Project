package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * State handling all logic related to the main menu
 */
class MainMenuState : GuiState() {

    private val menuList = listOf(
        Pair("Play") { GameStateManager.set(PreGameState()) },
        Pair("Settings") { GameStateManager.set(SettingsState()) },
        Pair("Matchmaking") { GameStateManager.set(MatchmakingState()) }

    )

    override val guiObjects: List<GuiObject> = menuList
        .mapIndexed { i, (name, func) ->
            GUI.menuButton(
                15f,
                80f - 20f * i,
                name,
                font = Font.MEDIUM_BLACK,
                color = Palette.GREY,
                borderColor = Palette.LIGHT_GREY,
                onClick = func
            )
        }

    private val title: GuiObject = GUI.text(
        Gdx.graphics.width / 2 - 250f,
        Gdx.graphics.height - 210f,
        500f,
        100f,
        "Treasure hunt",
        font = Font.XXL_BLACK

    )
    private val skull: GuiObject = GUI.image(
        Gdx.graphics.width / 2 - 36f,
        Gdx.graphics.height - 270f,
        72f,
        72f,
        "images/skull_and_crossbones.png"
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(title, skull, *guiObjects.toTypedArray())
    }
}
