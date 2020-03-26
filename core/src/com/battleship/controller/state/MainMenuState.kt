package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class MainMenuState : GuiState() {

    private val menuList = listOf(
        Pair("Play") { GameStateManager.set(PreGameState()) },
        Pair("Settings") { GameStateManager.set(SettingsState()) },
        Pair("Matchmaking") { GameStateManager.set(MatchmakingState()) }
    )

    override val guiObjects: List<GuiObject> = menuList
        .mapIndexed { i, (name, func) ->
            GUI.menuButton(
                Gdx.graphics.width / 2 - 170f,
                Gdx.graphics.height - (450f + 150f * i),
                name,
                onClick = func
            )
        }

    val header: GuiObject =  GUI.text(
        Gdx.graphics.width / 2 - 250f,
        Gdx.graphics.height - (150f),
        500f,
        100f,
        "Treasure hunt",
        font = Font.LARGE_BLACK

    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(header, *guiObjects.toTypedArray())
    }
}
