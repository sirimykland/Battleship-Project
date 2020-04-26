package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.PlayView
import com.battleship.view.View
// TODO Siri
class LoadingGameState(controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    var showDialog: Boolean = false
    var opponentLeftRenders: Int = 0

    private val header: GuiObject = GUI.header("Waiting for opponent to join...")

    /**
     * Called when user presses "leaves game" in dialog
     * Resets game and removes player from firebase entry
     */
    private fun leaveGame() {
        controller.leaveGame(GSM.activeGame!!.gameId, GSM.userId) {
            GSM.resetGame()
            controller.addPendingGamesListener { pendingGames ->
                GSM.pendingGames = pendingGames
            }
            GSM.set(MainMenuState(controller))
        }
    }

    private val opponentLeftDialog = GUI.dialog(
        "Your opponent left the game before registering treasures...",
        listOf(Pair("Find new game", {
            showDialog = false
            leaveGame()
        }))
    )

    /**
     * List of drawable gui and game objects
     */
    override val guiObjects: List<GuiObject> = listOf(
        header,
        GUI.backButton { leaveGame() },
        *opponentLeftDialog
    )

    /**
     * Called when the State should render itself.
     */
    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }

    /**
     * Updates as often as the game renders itself.
     *
     * @param dt: Float - delta time since last call
     */
    override fun update(dt: Float) {
        if (GSM.activeGame!!.opponentLeft) {
            if (opponentLeftRenders < 2) opponentLeftRenders++
            if (opponentLeftRenders == 1) showDialog = true // First opponent left render
        }
        if (showDialog) opponentLeftDialog.forEach { guiObject -> guiObject.show() }
        else opponentLeftDialog.forEach { guiObject -> guiObject.hide() }

        // Game is ready!
        if (GSM.activeGame!!.gameReady) GSM.set(PlayState(controller))

        updateHeaderText()
    }

    private fun updateHeaderText() {
        if (GSM.activeGame!!.opponent.playerId == "")
            header.set(Text("Waiting for opponent to join...", font = Font.MEDIUM_WHITE))
        else if (GSM.activeGame!!.opponent.board.isTreasureListEmpty())
            header.set(Text("Waiting for ${GSM.activeGame!!.opponent.playerName} to register treasures...", font = Font.MEDIUM_WHITE))
    }
}
