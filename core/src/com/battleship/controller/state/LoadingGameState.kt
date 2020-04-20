package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.PlayView
import com.battleship.view.View

class LoadingGameState(private var controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    var showDialog: Boolean = false
    var opponentLeftRenders: Int = 0

    override fun create() {
        super.create()
        println("---LOADINGSTATE---")
    }

    private fun headerText(): String {
        if (GSM.activeGame!!.opponent.playerId == "") return "Waiting for opponent to join..."
        else if (GSM.activeGame!!.opponent.board.treasures.isEmpty())
            return "Opponent joined! Waiting for ${GSM.activeGame!!.opponent.playerName} to register treasures..."
        return "null"
    }

    private val opponentLeftDialog = GUI.dialog(
        "${GSM.activeGame!!.opponent.playerName} left the game before registering treasures.",
        listOf(Pair("Find new game", {
            showDialog = false
            // TODO: Add some controller logic here?
            GSM.set(MatchmakingState(controller))
        }))
    )

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header(headerText()),
        GUI.backButton {
            controller.leaveGame(GSM.activeGame!!.gameId, GSM.userId) {
                GSM.resetGame()
                controller.addPendingGamesListener { pendingGames ->
                    GSM.pendingGames = pendingGames
                }
                GSM.set(MainMenuState(controller))
            }
        },
        *opponentLeftDialog
    )

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())

    }

    override fun update(dt: Float) {
        println("Opponent left the game: " + GSM.activeGame!!.opponentLeft)

        if (GSM.activeGame!!.opponentLeft) {
            if (opponentLeftRenders < 2) opponentLeftRenders++

            // First opponent left render, show GUI dialog
            if (opponentLeftRenders == 1) showDialog = true
        }

        if (showDialog) opponentLeftDialog.forEach { guiObject -> guiObject.show() }
        else opponentLeftDialog.forEach { guiObject -> guiObject.hide() }

        if (GSM.activeGame!!.gameReady) GSM.set(PlayState(controller))
    }

    override fun dispose() {
        super.dispose()
        // controller.detachGameListener(GSM.activeGame!!.gameId)
    }
}
