package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.battleship.controller.state.MenuState

class BattleshipGame : Game() {

    private var batch: SpriteBatch = SpriteBatch()
    companion object {
        val WIDTH = 480
        val HEIGHT = 800
    }
    override fun create() {
        GameStateManager.push(MenuState())
    }

    override fun dispose() {
    }

    override fun render() {
        super.render()
        GameStateManager.render(batch)
        GameStateManager.update(Gdx.graphics.deltaTime)
    }
}
