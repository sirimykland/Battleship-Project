package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class GameOverState : GuiState() {
    private val menuList = listOf(
        Pair("Main Menu") { GameStateManager.set(MainMenuState()) },
        Pair("Play Again") { GameStateManager.set(PreGameState()) }
    )

    override val guiObjects: List<GuiObject> = listOf(
        GUI.textButton(
            Gdx.graphics.width / 2 - 170f,
            Gdx.graphics.height - 560f,
            340f,
            140f,
            "Main Menu",
            font = Font.MEDIUM_BLACK,
            color = Palette.GREY,
            borderColor = Palette.LIGHT_GREY,
            onClick = { GameStateManager.set(MainMenuState()) }
        ),
        GUI.textButton(
            Gdx.graphics.width / 2 - 170f,
            Gdx.graphics.height - 380f,
            340f,
            140f,
            "Play Again",
            font = Font.MEDIUM_BLACK,
            color = Palette.GREY,
            borderColor = Palette.LIGHT_GREY,
            onClick = { GameStateManager.set(MatchmakingState()) }
        ),
        GUI.text(
            20f,
            Gdx.graphics.height - 220f,
            Gdx.graphics.width - 40f,
            90f,
            "GAME OVER",
            Font.LARGE_WHITE
        )
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}