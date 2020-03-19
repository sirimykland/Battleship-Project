package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.BattleshipGame
import com.battleship.GameStateManager
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class SettingsState : GuiState() {
    override var view: View = BasicView()

    private var soundButton: GuiObject = GUI.textButton(
        Gdx.graphics.width / 2 - 170f,
        Gdx.graphics.height - 380f,
        340f,
        140f,
        "Sound off",
        font = Font.MEDIUM_WHITE,
        color = Palette.BLACK,
        borderColor = Palette.BLUE,
        onClick =  {
            if(BattleshipGame.music?.isPlaying == true)
                BattleshipGame.music?.pause()
            else
                BattleshipGame.music?.play()
        }
    )
    override val guiObjects: List<GuiObject> = listOf(
        soundButton,
        GUI.textButton(
            Gdx.graphics.width / 2 - 170f,
            Gdx.graphics.height - 560f,
            340f,
            140f,
            "Help",
            font = Font.MEDIUM_WHITE,
            color = Palette.BLACK,
            borderColor = Palette.BLUE,
            onClick = { GameStateManager.set(HelpState()) }
        ),
        GUI.text(
            20f,
            Gdx.graphics.height - 220f,
            Gdx.graphics.width - 40f,
            90f,
            "Settings",
            Font.LARGE_WHITE
        ),
        GUI.text(
            20f,
            80f,
            Gdx.graphics.width - 40f,
            90f,
            "Legal stuff",
            Font.SMALL_WHITE
        ),
        GUI.text(
            20f,
            20f,
            Gdx.graphics.width - 40f,
            90f,
            "v0.0.1",
            Font.SMALL_WHITE
        ),
        GUI.backButton
    )

    private fun updateButtons() {
        if(BattleshipGame.music?.isPlaying == true) {
            // TODO: Not set text if text is equal
            soundButton.set(Text("Sound off"))
        } else if (BattleshipGame.music?.isPlaying == false){
            soundButton.set(Text("Sound on"))
        } else {
            soundButton.set(Text("No sound"))
        }

    }
    override fun create() {
        super.create()
        updateButtons()
    }

    override fun update(dt: Float) {
        updateButtons()
    }

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}
