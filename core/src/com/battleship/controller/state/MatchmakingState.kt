package com.battleship.controller.state

import com.battleship.controller.firebase.FindPlayer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.view.MatchmakingView
import com.battleship.view.View

class MatchmakingState : State() {
    override var view: View = MatchmakingView()
    override var firebaseController: FirebaseController = FindPlayer()

    override fun create() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun update(dt: Float) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun render() {
        // this.view.render()
    }

    override fun dispose() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
