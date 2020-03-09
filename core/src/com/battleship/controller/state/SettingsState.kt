package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FindPlayer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.Header
import com.battleship.model.ui.TextButton
import com.battleship.view.SettingsView
import com.battleship.view.View

class SettingsState : MenuState() {

    override var view: View = SettingsView()
    override var firebaseController: FirebaseController = FindPlayer()

    private val settingsButtons: Array<TextButton> = arrayOf(*(0..2).map { a: Int -> goToSetting(a) }.toTypedArray())

    override val buttons: List<Button> = listOf(
        TextButton(20f, Gdx.graphics.height - 110f, 100f, 90f, "<-") { GameStateManager.pop() },
        *settingsButtons
    )

    private var settings = emptyMap<String, String>()
    private var settingsList = emptyList<String>()

    private val uiElements = arrayOf(
        Header(20f, Gdx.graphics.height - 120f, Gdx.graphics.width - 40f, 90f, "Settings"),
        *buttons.toTypedArray()
    )

    override fun create() {
        super.create()
        settings = mapOf(
            Pair("Sound on", "a73ab"),
            Pair("Your profile", "6b293"),
            Pair("About the app", "9c99d")
        )
        settingsList = settings.toList().map { a -> a.first }
        settingsButtons.forEachIndexed { i, button ->
            button.text = settingsList[i]
        }
    }

    fun goToSetting(index: Int): TextButton {
        return TextButton(
            50f, Gdx.graphics.height - 330f - index * 110f,
            Gdx.graphics.width - 80f, 90f, "Loading") {
            println(settingsList[index])
        }
    }

    override fun update(dt: Float) {
    }

    override fun render() {
        this.view.render(*uiElements)
    }
}
