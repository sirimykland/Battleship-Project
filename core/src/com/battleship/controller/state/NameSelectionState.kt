package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

class NameSelectionState(val controller: FirebaseController) : GuiState(controller) {
    private var username = GSM.username

    private val legalCharacters = "abcdefghijklmnopqrstuvwxyzæøåABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ1234567890"

    private val usernameDisplay = GUI.textBox(
        15f,
        50f,
        70f,
        10f,
        username
    ).onKeyTyped { char ->
        when (char) {
            '\b' -> username = if (username.isNotEmpty()) username.substring(0, username.length - 1) else username
            '\r', '\n' -> complete()
            in legalCharacters -> username += char
        }
    }

    private val submitButton = GUI.menuButton(25f, 25f, "Continue") { complete() }

    private fun complete() {
        if (username != "") {
            controller.addPlayer(username)
            GSM.push(MatchmakingState(controller))
        }
    }

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Choose your username"),
        usernameDisplay,
        submitButton,
        GUI.backButton { GSM.set(MainMenuState(controller)) }
    )
    override var view: View = BasicView()

    override fun update(dt: Float) {
        usernameDisplay.set(Text(username, font = Font.MEDIUM_BLACK))
    }

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }

    override fun create() {
        super.create()
        Gdx.input.setOnscreenKeyboardVisible(true)
    }

    override fun dispose() {
        super.dispose()
        Gdx.input.setOnscreenKeyboardVisible(false)
    }
}
