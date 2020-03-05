package com.battleship.controller.state

import com.battleship.controller.firebase.FindPlayer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.TextButton
import com.battleship.view.MatchmakingView
import com.battleship.view.View

class MatchmakingState : MenuState() {
    override var view: View = MatchmakingView()
    override var firebaseController: FirebaseController = FindPlayer()

    val button = TextButton(50f, 300f, 500f, 150f, "Hello World") { println("Hello World") }

    override val buttons: List<Button> = listOf(button)

    override fun update(dt: Float) {
    }

    override fun render() {
        this.view.render(*buttons.toTypedArray())
    }

    override fun dispose() {
    }
}
