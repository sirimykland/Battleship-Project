package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject

/**
 * Abstract state class declaring functions and variables for GUI states
 */
// TODO Karl
abstract class GuiState(controller: FirebaseController) : State(controller) {
    abstract val guiObjects: List<GuiObject>
    private var multiplexer: InputMultiplexer = InputMultiplexer()

    /**
     * Called once when the State is first initialized.
     */
    override fun create() {
        Gdx.input.inputProcessor =
            InputMultiplexer(
                *guiObjects
                    .filter { it.hasListener }
                    .map { it.listener }
                    .reversed()
                .toTypedArray()
            )
    }

    /**
     * Called when the State is paused, usually when it's not active or visible on-screen.
     * Is also paused before state is destroyed.
     */
    override fun pause() {
        Gdx.input.inputProcessor = InputMultiplexer()
        super.pause()
    }

    /**
     * Called when the State is resumed from a paused state, usually when it regains focus.
     */
    override fun resume() {
        Gdx.input.inputProcessor =
            InputMultiplexer(
                *guiObjects
                .filter { it.hasListener }
                .map { it.listener }
                .reversed()
                .toTypedArray()
            )
        super.resume()
    }

    /**
     * Called once when the State is destroyed.
     */
    override fun dispose() {
        super.dispose()
        Gdx.input.inputProcessor = null
    }
}
