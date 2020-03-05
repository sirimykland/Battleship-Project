package com.battleship.controller.state

import com.badlogic.gdx.math.Vector2
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.TextBox
import com.battleship.model.ui.TextButton
import com.battleship.view.MatchmakingView
import com.battleship.view.View

class TestMenuState : MenuState() {

    val button = TextButton(100f, 200f, 300f, 150f, "tect") { println("aosidf") }
    val textbox = TextBox(Vector2(420f, 400f), Vector2(100f, 100f), "bi2wds")
    override val buttons: List<Button> = listOf(button)
    override var view: View = MatchmakingView()
    override var firebaseController: FirebaseController
        get() = TODO("not implemented") // To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*buttons.toTypedArray(), textbox)
    }

    override fun dispose() {
    }
}
