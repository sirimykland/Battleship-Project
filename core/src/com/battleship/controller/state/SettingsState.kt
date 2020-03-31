package com.battleship.controller.state

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
                15f,
                60f - 20f * i,
                70f,
                16f,
                name,
                font = Font.MEDIUM_WHITE,
                color = Palette.BLACK,
                borderColor = Palette.BLUE,
                onClick = func
            )
        }.toTypedArray(),

        GUI.text(
            5f,
            80f,
            90f,
            10f,
            "Settings",
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
