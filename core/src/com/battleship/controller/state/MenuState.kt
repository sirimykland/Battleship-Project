package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.battleship.model.ui.Button

abstract class MenuState : State() {
    abstract val buttons: List<Button>

    override fun create() {
        Gdx.input.inputProcessor = InputMultiplexer(*buttons.map { it.listener }.toTypedArray())
    }

    override fun dispose() {
        super.dispose()
        Gdx.input.inputProcessor = null
    }
}
