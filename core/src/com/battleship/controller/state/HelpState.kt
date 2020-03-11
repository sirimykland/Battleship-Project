package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FindPlayer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.TextButton
import com.battleship.view.BasicView
import com.battleship.view.View

class HelpState : MenuState() {

    override var view: View = BasicView()
    // TODO: Remove firebaseController here when it is not included in State anymore
    override var firebaseController: FirebaseController = FindPlayer()

    override val buttons: List<Button> = listOf(TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 200f, 300f, 150f, "Matchmaking") { GameStateManager.set(MatchmakingState()) },
        TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 800f, 300f, 150f, "Testmenu") { GameStateManager.set(TestMenuState()) },
        TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 400f, 300f, 150f, "Settings") { GameStateManager.set(SettingsState()) },
        TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 600f, 300f, 150f, "Play") { GameStateManager.set(PlayState()) })

    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*buttons.toTypedArray())
    }
}
