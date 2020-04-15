package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class NameSelectionState(val controller: FirebaseController) : GuiState(controller) {
    private var username = ""

    private val legalCharacters = "abcdefghijklmnopqrstuvwxyzæøåABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ1234567890"

    private val usernameDisplay = GUI.textBox(
        15f,
        55f,
        70f,
        10f,
        username,
        font = Font.SMALL_BLACK,
        color = Palette.WHITE,
        borderColor = Palette.BLACK
    ).onKeyTyped { char ->
        when (char) {
            '\b' -> {
                username = if (username.isNotEmpty()) username.substring(0, username.length - 1) else username
            }
            '\r', '\n' -> {
                complete()
            }
            in legalCharacters -> username += char
        }
    }

    private val submitButton = GUI.textButton(
        25f, 35f, 50f, 15f, "Submit") {
        complete()
    }

    private fun complete() {
        println("complete $username")
        controller.addPlayer(username)
        GameStateManager.pop()
    }

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Choose a username"),
        usernameDisplay,
        submitButton
    )
    override var view: View = BasicView()

    override fun update(dt: Float) {
        usernameDisplay.set(Text(username, font = Font.SMALL_BLACK))
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
