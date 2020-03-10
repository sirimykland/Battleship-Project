package com.battleship.controller.state

import com.battleship.BattleshipGame
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.TextButton
import com.battleship.view.MainMenuView
import com.battleship.view.View



class MainMenuState : MenuState() {

    val testButton = TextButton(BattleshipGame.WIDTH/10, BattleshipGame.HEIGHT/10, 300f, 150f, "TestMenu") { GameStateManager.set(MatchmakingState()) }
    val settingsButton = TextButton(BattleshipGame.WIDTH/10, BattleshipGame.HEIGHT/2, 300f, 150f, "Settings") { GameStateManager.set(PlayState()) }

    override val buttons: List<Button> = listOf(testButton, settingsButton)
    override var view: View = MainMenuView()
    override var firebaseController: FirebaseController
        get() = TODO("not implemented") // To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*buttons.toTypedArray())
    }

    override fun dispose() {
    }
}
