package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.BattleshipGame
import com.battleship.GameStateManager
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

/**
 * State handling all logic related to the settings menu
 */
class SettingsState : GuiState() {
    override var view: View = BasicView()
    private var soundButton: GuiObject = GUI.menuButton(
            Gdx.graphics.width / 2 - 170f,
            Gdx.graphics.height - 450f,
            "Sound off",
            onClick = {
                if (BattleshipGame.music?.isPlaying == true)
                    BattleshipGame.music?.pause()
                else
                    BattleshipGame.music?.play()
            }
    )
    override val guiObjects: List<GuiObject> = listOf(
            soundButton,
            GUI.menuButton(
                    Gdx.graphics.width / 2 - 170f,
                    Gdx.graphics.height - 600f,
                    "Usage guide",
                    onClick = { GameStateManager.set(HelpState()) }
            ),
            GUI.header("Settings"),
            GUI.text(
                    20f,
                    80f,
                    Gdx.graphics.width - 40f,
                    90f,
                    "Legal stuff",
                    Font.SMALL_BLACK
            ),
            GUI.text(
                    20f,
                    20f,
                    Gdx.graphics.width - 40f,
                    90f,
                    "v0.0.1",
                    Font.SMALL_BLACK
            ),
            GUI.backButton
    )

    override fun create() {
        super.create()
        updateButtons()
    }

    private fun updateButtons() {
        if (false == true) { // TODO: Remove when done
            if (BattleshipGame.music?.isPlaying == true)
                soundButton.set(Text("Sound off"))
            else
                soundButton.set(Text("Sound on"))
        }
    }

    override fun update(dt: Float) {
        updateButtons()
    }

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}
