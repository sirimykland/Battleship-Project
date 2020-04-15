package com.battleship.controller.state

import com.battleship.view.View

/**
 * Abstract state class declaring common functions and variables
 */
abstract class State {
    abstract var view: View

    abstract fun create()

    abstract fun update(dt: Float)

    abstract fun render()

    open fun pause() {}

    open fun resume() {}

    open fun dispose() {
        view.dispose()
    }
}
