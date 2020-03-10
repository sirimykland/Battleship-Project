package com.battleship

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.battleship.controller.state.SettingsState
import com.battleship.utility.Font

class BattleshipGame : Game() {

    companion object {
        val WIDTH = 480f
        val HEIGHT = 800f
    }
    override fun create() {
        GameStateManager.push(SettingsState())
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
