package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.battleship.controller.state.MatchmakingState
import com.battleship.utility.Font

class BattleshipGame : Game() {

    override fun create() {
        GameStateManager.push(MatchmakingState())
    }

    override fun dispose() {
        Font.dispose()
    }

    override fun render() {
        super.render()
        GameStateManager.render()
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
