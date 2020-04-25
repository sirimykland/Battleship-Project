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
// TODO Karl
class NameSelectionState(controller: FirebaseController) : GuiState(controller) {
    private var username = GSM.username

    private val legalCharacters =
        "abcdefghijklmnopqrstuvwxyzæøåABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ1234567890"

    private val usernameDisplay = GUI.textBox(
        15f,
        75f,
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

    private fun complete() {
        if (username != "") {
            GSM.username = username
            GSM.set(MatchmakingState(controller))
        }
    }

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Choose your username"),
        usernameDisplay,
        submitButton,
        GUI.backButton { GSM.set(MainMenuState(controller)) }
    )
    override var view: View = BasicView()

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
