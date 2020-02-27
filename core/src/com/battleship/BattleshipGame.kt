package com.battleship


import com.badlogic.gdx.Game
import com.battleship.controller.state.PlayState

class BattleshipGame : Game() {

    override fun create() {
        GameStateManager.push(PlayState())
    }

    override fun dispose() {

    }

    override fun render() {
        super.render()
        GameStateManager.render()
    }
}
