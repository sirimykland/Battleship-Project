package com.battleship.controller.state

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
            23.44f,
            62.5f,
            "Music off",
            onClick = {
                if (BattleshipGame.music?.isPlaying == true)
                    BattleshipGame.music?.pause()
                else
                    BattleshipGame.music?.play()
            }
    )
    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Settings"),
        soundButton,
        GUI.menuButton(
            23.44f,
            43.75f,
            "Sound effects on",
            onClick = { print("Sound effects on/off") }),
        GUI.menuButton(
            23.44f,
            25f,
            "Usage guide",
            onClick = { GameStateManager.set(UsageGuideState()) }
        ),

        GUI.text(
            3f,
            10f,
            94f,
            11.25f,
            "Legal stuff",
            Font.SMALL_BLACK
        ),
        GUI.text(
            3f,
            5f,
            94f,
            11.25f,
            "v0.1.0",
            Font.SMALL_BLACK
        ),
        GUI.backButton { GameStateManager.set(MainMenuState()) }
    )

    override fun create() {
        super.create()
        updateButtons()
    }

    /**
     * Update button text based on music status
     */
    private fun updateButtons() {
        if (BattleshipGame.music?.isPlaying == true)
            soundButton.set(Text("Music off", Font.MEDIUM_BLACK))
        else
            soundButton.set(Text("Music on", Font.MEDIUM_BLACK))
    }

    override fun update(dt: Float) {
        updateButtons()
    }

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}
