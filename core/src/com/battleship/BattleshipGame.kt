package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
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
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
