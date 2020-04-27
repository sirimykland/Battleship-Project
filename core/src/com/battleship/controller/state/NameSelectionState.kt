package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.controller.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View
/**
 * Create and handle components in the name selection menu
 *
 * Inherits behavior from [GuiState]
 *
 * @param controller: FirebaseController - interface handling storing and retrieving data from Firebase
 */
class NameSelectionState(controller: FirebaseController) : GuiState(controller) {
    override var view: View = BasicView()
    private var username = GSM.username

    private val legalCharacters =
        "abcdefghijklmnopqrstuvwxyzæøåABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ1234567890"

    private val usernameDisplay = GUI.textBox(
        15f,
        70f,
        70f,
        10f,
        username
    ).onKeyTyped { char ->
        when (char) {
            '\b' -> username =
                if (username.isNotEmpty()) username.substring(0, username.length - 1) else username
            '\r', '\n' -> complete()
            in legalCharacters -> username += char
        }
    }.onClick {
        Gdx.input.setOnscreenKeyboardVisible(true)
    }

    private val submitButton = GUI.menuButton(25f, 50f, "Continue") { complete() }

    /**
     * Saves the selected username to the [GSM] and changes state to [MatchmakingState]
     */
    private fun complete() {
        if (username != "") {
            GSM.username = username
            GSM.set(MatchmakingState(controller))
        }
    }

    /**
     * List of drawable gui and game objects
     */
    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Choose your username"),
        usernameDisplay,
        submitButton,
        GUI.backButton { GSM.set(MainMenuState(controller)) }
    )

    /**
     * Updates as often as the game renders itself.
     *
     * @param dt: Float - delta time since last call
     */
    override fun update(dt: Float) {
        usernameDisplay.set(Text(username, font = Font.MEDIUM_BLACK))
    }

    /**
     * Called when the State should render itself.
     */
    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }

    /**
     * Called once when the State is first initialized.
     */
    override fun create() {
        super.create()
        Gdx.input.setOnscreenKeyboardVisible(true)
    }

    /**
     * Called once when the State is destroyed.
     */
    override fun dispose() {
        super.dispose()
        Gdx.input.setOnscreenKeyboardVisible(false)
    }
}
