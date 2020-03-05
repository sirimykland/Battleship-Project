package com.battleship.controller.state

import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.firebase.UpdatePlayData
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.battleship.view.MenuView
import com.battleship.view.View
import com.battleship.BattleshipGame

class MenuState : State() {

    override var view: View = MenuView()

    override var firebaseController: FirebaseController = UpdatePlayData()
    // TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.



    override fun create() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(sb: SpriteBatch) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        view.render(sb)
    }

    override fun update(dt: Float) {
        // TODO("not implemented") // To change body of created functions use File | Settings | File Templates.

    }

    override fun dispose() {
        // TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}