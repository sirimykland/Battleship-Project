package com.battleship.controller.state

import com.battleship.controller.firebase.FirebaseController
import com.battleship.view.View

abstract class State {

    abstract var view: View
    abstract var firebaseController: FirebaseController

    abstract fun create()

    abstract fun update(dt: Float)

    abstract fun render()

    open fun dispose() {
        view.dispose()
    }
}
