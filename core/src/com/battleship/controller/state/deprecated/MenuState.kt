package com.battleship.controller.state.deprecated

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.state.State
import com.battleship.model.ui.deprecated.Button

@Deprecated("Use GuiState instead")
abstract class MenuState(private val controller : FirebaseController) : State(controller) {
    abstract val buttons: List<Button>

    override fun create() {
        Gdx.input.inputProcessor = InputMultiplexer(*buttons.map { it.listener }.toTypedArray())
    }

    override fun dispose() {
        super.dispose()
        Gdx.input.inputProcessor = null
    }
}
