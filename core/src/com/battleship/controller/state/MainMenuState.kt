package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.model.ui.Button
import com.battleship.model.ui.TextButton
import com.battleship.view.BasicView
import com.battleship.view.View

class MainMenuState : MenuState() {

    val mathchmakingButton = TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 200f, 300f, 150f, "Matchmaking") { GameStateManager.set(MatchmakingState()) }
    val settingsButton = TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 400f, 300f, 150f, "Settings") { GameStateManager.set(SettingsState()) }
    val playButton = TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 600f, 300f, 150f, "Play") { GameStateManager.set(PlayState()) }
    val testButton = TextButton(Gdx.graphics.width / 2 - 150f, Gdx.graphics.height - 800f, 300f, 150f, "Testmenu") { GameStateManager.set(TestMenuState()) }

    override val buttons: List<Button> = listOf(mathchmakingButton, testButton, settingsButton, playButton)
    override var view: View = BasicView()

    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*buttons.toTypedArray())
    }
}
