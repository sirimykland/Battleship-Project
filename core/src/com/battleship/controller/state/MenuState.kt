package com.battleship.controller.state

import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.firebase.UpdatePlayData
import com.battleship.view.MenuView
import com.battleship.view.View
//import android.R.attr.button


class MenuState : State() {

    override var view: View = MenuView()


    override var firebaseController: FirebaseController = UpdatePlayData()
    // TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.



    override fun create() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun render() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        this.view.render()
    }

    override fun update(dt: Float) {
        // TODO("not implemented") // To change body of created functions use File | Settings | File Templates.

    }

    override fun dispose() {
        // TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}