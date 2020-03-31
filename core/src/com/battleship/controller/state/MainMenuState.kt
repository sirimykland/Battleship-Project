package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class MainMenuState : GuiState() {

    private val menuList = listOf(
        Pair("Matchmaking") { GameStateManager.set(MatchmakingState()) },
        Pair("Settings") { GameStateManager.set(SettingsState()) },
        Pair("Play") { GameStateManager.set(PlayState()) }
    )

    override val guiObjects: List<GuiObject> = menuList
        .mapIndexed { i, (name, func) ->
            GUI.textButton(
                15f,
                80f - 20f * i,
                70f,
                16f,
                name,
                font = Font.MEDIUM_BLACK,
                color = Palette.GREY,
                borderColor = Palette.LIGHT_GREY,
                onClick = func
            )
        }

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}
