package com.battleship.controller.state

import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.firebase.GameController
import com.battleship.model.Player
import com.battleship.view.PlayView
import com.battleship.view.View

class PlayState : State() {

    override var view: View = PlayView()

    override var firebaseController: FirebaseController = GameController()

    var player: Player = Player()

    override fun create() {
    }

    override fun render() {
        this.view.render(player.board)
    }

    override fun update(dt: Float) {
    }

    override fun dispose() {
    }
}
