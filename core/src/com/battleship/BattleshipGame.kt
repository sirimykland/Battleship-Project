package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.battleship.controller.state.MatchmakingState
import com.battleship.controller.state.PlayState

class BattleshipGame : Game() {

    override fun create() {
        GameStateManager.push(PlayState())
        GameStateManager.push(MatchmakingState())
    }

    override fun dispose() {
    }

    override fun render() {
        super.render()
        GameStateManager.render()
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
