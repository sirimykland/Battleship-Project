package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.battleship.controller.state.MainMenuState
import com.battleship.controller.state.TestMenuState


class BattleshipGame : Game() {

    companion object {
        val WIDTH = 480
        val HEIGHT = 800
    }
    override fun create() {
        GameStateManager.push(TestMenuState())

    }

    override fun dispose() {
    }

    override fun render() {
        super.render()
        GameStateManager.render()
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
