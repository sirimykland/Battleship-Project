package com.battleship.controller.state

import com.battleship.controller.firebase.FirebaseController
import com.battleship.view.View

abstract class State {

    abstract val view:View
    abstract val firebaseController: FirebaseController

    abstract fun create()

    abstract fun update(dt: Float)

    fun render(){
        view.render()
    }

    abstract fun dispose()
}