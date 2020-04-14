package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject

/**
 * Abstract state class declaring functions and variables for GUI states
 */
abstract class GuiState(controller: FirebaseController) : State(controller) {
    abstract val guiObjects: List<GuiObject>
    private var multiplexer: InputMultiplexer = InputMultiplexer();

    override fun create() {
        multiplexer.addProcessor(InputMultiplexer(*guiObjects.filter { it.isClickable }
            .map { it.listener }
            .toTypedArray()))
        Gdx.input.inputProcessor = multiplexer
    }

    override fun dispose() {
        super.dispose()
        Gdx.input.inputProcessor = null
    }

    fun addInputProcessor(input: InputAdapter) {
        multiplexer.addProcessor(input)
    }
}
