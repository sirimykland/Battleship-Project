package com.battleship.controller.state

import com.battleship.view.View

abstract class State {
    abstract var view: View

    abstract fun create()

    abstract fun update(dt: Float)

    abstract fun render()

    open fun dispose() {
        view.dispose()
    }
}
