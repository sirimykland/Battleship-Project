package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.GameController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.PlayView
import com.battleship.view.View

class LoadingGameState(var gameController: GameController) : GuiState() {
    override var view: View = PlayView()

    override fun create() {
        super.create()
        println("---LOADINGSTATE---")
        println("gameready is: " + GSM.activeGame.gameReady)
        gameController.addGameListener(GSM.activeGame.gameId)
    }

    private fun headerText(): String {
        if (GSM.activeGame.opponent.playerId == "") return "Waiting for opponent to join..."
        else if (GSM.activeGame.opponent.board.treasures.isEmpty()) return "Waiting for opponent to register ships..."
        return "null"
    }

    override val guiObjects: List<GuiObject> = listOf(
            GUI.header("Waiting for opponent to register..."),
            GUI.backButton { GSM.set(MainMenuState()) }
    )

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }

    override fun update(dt: Float) {
        if (GSM.activeGame.gameReady) {
            GSM.set(PlayState(gameController))
        }
    }

    override fun dispose() {
        super.dispose()
        // gameController.detachGameListener(GSM.activeGame.gameId)
    }
}
