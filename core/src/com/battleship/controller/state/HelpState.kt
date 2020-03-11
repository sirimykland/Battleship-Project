package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FindPlayer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.Header
import com.battleship.model.ui.Image
import com.battleship.model.ui.TextBox
import com.battleship.model.ui.TextButton
import com.battleship.view.BasicView
import com.battleship.view.View

class HelpState : MenuState() {

    override var view: View = BasicView()
    // TODO: Remove firebaseController here when it is not included in State anymore
    override var firebaseController: FirebaseController = FindPlayer()

    override val buttons: List<Button> = listOf(
        TextButton(20f, Gdx.graphics.height - 110f, 150f, 90f, "Back") { GameStateManager.set(MainMenuState()) }
    )

    private val uiElements = arrayOf(
        Header(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 130f, 300f, 150f, "Help"),
        TextBox(20f, 20f, Gdx.graphics.width - 40f, 90f, "Blablablabla"),
        Image(200f, Gdx.graphics.height / 2 - 100f, "badlogic.jpg"),
        *buttons.toTypedArray()

    )

    override fun update(dt: Float) {

    }

    override fun render() {
        view.render(*uiElements)
    }
}
