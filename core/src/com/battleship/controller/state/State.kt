package com.battleship.controller.state

import com.battleship.controller.firebase.FirebaseController
import com.battleship.view.View

/**
 * Abstract State class declaring common functions and variables
 *
 * @param controller: FirebaseController - interface handling storing and retrieving data from Firebase
 */
abstract class State(protected val controller: FirebaseController) {
    abstract var view: View

    /**
     * Called once when the State is first initialized.
     */
    abstract fun create()

    /**
     * Updates as often as the game renders itself.
     *
     * @param dt: Float - delta time since last call
     */
    open fun update(dt: Float) {}

    /**
     * Called when the State should render itself.
     */
    abstract fun render()

    /**
     * Called when the State is paused, usually when it's not active or visible on-screen.
     * Is also paused before state is destroyed.
     */
    open fun pause() {}

    /**
     * Called when the State is resumed from a paused state, usually when it regains focus.
     */
    open fun resume() {}

    /**
     * Called once when the State is destroyed.
     */
    open fun dispose() {
        view.dispose()
    }
}
