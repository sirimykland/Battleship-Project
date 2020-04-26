package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject

/**
 * Abstract state class declaring functions and variables for GUI states
 *
 * Inherits behavior from [State]
 *
 * @param controller: FirebaseController - interface handling storing and retrieving data from Firebase
 */
abstract class GuiState(controller: FirebaseController) : State(controller) {

    /**
     * List of drawable gui and game objects
     */
    abstract val guiObjects: List<GuiObject>

    /**
     * Called once when the State is first initialized.
     * Creates an [InputMultiplexer] which combines all listeners on all objects in [guiObjects] and
     * sets that as the active [InputProcessor] for the app
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
