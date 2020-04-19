package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.PlayView
import com.battleship.view.View

class LoadingGameState(private var controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()

    override fun create() {
        super.create()
        println("---LOADINGSTATE---")
    }

    private fun headerText(): String {
        if (GSM.activeGame!!.opponent.playerId == "") return "Waiting for opponent to join..."
        else if (GSM.activeGame!!.opponent.board.treasures.isEmpty()) return "Waiting for opponent to register treasures..."
        return "null"
    }

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header(headerText()),
        GUI.backButton { GSM.set(MainMenuState(controller)) },
        GuiObject(0f, 0f, 100f, 100f).onClick {
            println(GSM.activeGame)
        }
    )

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }

    override fun update(dt: Float) {
        GSM.activeGame!!.setGameReadyifReady()
        if (GSM.activeGame!!.gameReady) {
            println("GAME is READY: " + GSM.activeGame!!.gameReady)
            GSM.set(PlayState(controller))
        }
    }

    override fun dispose() {
        super.dispose()
        // controller.detachGameListener(GSM.activeGame!!.gameId)
    }
}
