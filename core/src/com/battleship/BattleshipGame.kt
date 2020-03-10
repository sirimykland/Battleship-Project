package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.battleship.controller.state.MainMenuState
import com.battleship.controller.state.MatchmakingState
import com.battleship.controller.state.PlayState

class BattleshipGame : Game() {

    companion object {
        val WIDTH = 480f
        val HEIGHT = 800f
    }
    override fun create() {

        //GameStateManager.push(TestMenuState())
        GameStateManager.push(MainMenuState())
    }

    override fun dispose() {
    }

    override fun render() {
        super.render()
        GameStateManager.render()
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
