package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class SettingsState : GuiState() {

    private val settingsList = listOf(
        Pair("Sound on") { println("Sound on") },
        Pair("Your profile") { println("Your profile") },
        Pair("About the app") { println("About the app") }
    )

    override val guiObjects: List<GuiObject> = listOf(
        *settingsList.mapIndexed { i, (name, func) ->
            GUI.textButton(
                Gdx.graphics.width / 2 - 170f,
                Gdx.graphics.height - (380f + 180f * i),
                340f,
                140f,
                name,
                font = Font.MEDIUM_WHITE,
                color = Palette.BLACK,
                borderColor = Palette.BLUE,
                onClick = func
            )
        }.toTypedArray(),

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
            220f,
            Gdx.graphics.width - 40f,
            90f,
            "Write stuff about the app here",
            Font.LARGE_WHITE
        ),
        GUI.backButton
    )

    override var view: View = BasicView()

    override fun update(dt: Float) {}

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}
