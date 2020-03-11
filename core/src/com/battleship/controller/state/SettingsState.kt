package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FindPlayer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.Header
import com.battleship.model.ui.TextButton
import com.battleship.view.BasicView
import com.battleship.view.View

class SettingsState : MenuState() {

    override var view: View = BasicView()
    // TODO: Remove firebaseController here when it is not included in State anymore
    override var firebaseController: FirebaseController = FindPlayer()

    override val buttons: List<Button> = listOf(
        TextButton(20f, Gdx.graphics.height - 110f, 150f, 90f, "Back") { GameStateManager.set(MainMenuState()) },
        TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 300f, 300f, 150f, "Sound on") { println("Turn on/off sound") },
        TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 500f, 300f, 150f, "Help") { GameStateManager.set(HelpState()) }
    )

    private val uiElements = arrayOf(
        Header(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 130f, 300f, 150f, "Settings"),
        *buttons.toTypedArray()
    )

    override fun update(dt: Float) {
    }

    override fun render() {
        this.view.render(*uiElements)

    }
}
