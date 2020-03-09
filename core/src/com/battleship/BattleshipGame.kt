package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.battleship.controller.state.PlayState
import com.battleship.controller.state.SettingsState

class BattleshipGame : Game() {

    override fun create() {
        GameStateManager.push(PlayState())
        GameStateManager.push(SettingsState())
    }

    override fun dispose() {
    }

    override fun render() {
        super.render()
        GameStateManager.render()
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
