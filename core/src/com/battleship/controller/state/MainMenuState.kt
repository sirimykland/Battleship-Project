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
        Pair("Matchmaking") { GameStateManager.set(MatchmakingState()) },
        Pair("Settings") { GameStateManager.set(SettingsState()) },
        Pair("Play") { GameStateManager.set(PreGameState()) },
        Pair("GameOver") { GameStateManager.set(GameOverState()) }
    )

    override val guiObjects: List<GuiObject> = menuList
        .mapIndexed { i, (name, func) ->
            GUI.textButton(
                Gdx.graphics.width / 2 - 170f,
                Gdx.graphics.height - (200f + 180f * i),
                340f,
                140f,
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
