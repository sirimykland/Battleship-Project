package com.battleship.controller.state

import com.battleship.controller.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.input.TreasureHandler
import com.battleship.model.treasures.Treasure
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.PlayView
import com.battleship.view.View

/**
 * Create and handle components in the pre-game state placing ships on the board
 *
 * Inherits behavior from [GuiState]
 *
 * @param controller: FirebaseController - interface handling storing and retrieving data from Firebase
 */
class PreGameState(controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    private var showDialog: Boolean = false
    private var opponentLeftRenders: Int = 0

    /**
     * Called once when the State is first initialized.
     * Initializes the player with some treasures
     * and starts listening for database changes
     */
    override fun create() {
        super.create()
        GSM.activeGame!!.player.board.clearTreasures()
        GSM.activeGame!!.player.board.createAndPlaceTreasures(
            1,
            Treasure.TreasureType.GOLDKEY,
            true
        )
        GSM.activeGame!!.player.board.createAndPlaceTreasures(
            1,
            Treasure.TreasureType.GOLDCOIN,
            true
        )
        GSM.activeGame!!.player.board.createAndPlaceTreasures(
            1,
            Treasure.TreasureType.TREASURECHEST,
            true
        )
        controller.addGameListener(GSM.activeGame!!.gameId, GSM.activeGame!!.player.playerId)
    }

    private val readyButton = GUI.textButton(
        5f,
        3f,
        90f,
        10f,
        "Start Game",
        onClick = {
            val game = GSM.activeGame!!
            controller.registerTreasures(
                game.gameId,
                game.player.playerId,
                game.player.board.getTreasuresList()
            )
            GSM.set(LoadingGameState(controller))
        }
    )

    private val opponentLeftDialog = GUI.dialog(
        "Your opponent left the game...",
        listOf(Pair("Find new game", {
            showDialog = false
            leaveGame()
        }))
    )

    /**
     * List of drawable gui and game objects
     */
    override val guiObjects: List<GuiObject> = listOf(
        readyButton,
        GUI.header("Place treasures"),
        GUI.backButton { leaveGame() },
        GUI.listener("treasureHandler", TreasureHandler(GSM.activeGame!!.player.board)),
        *opponentLeftDialog
    )

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

    /**
     * Called when the State should render itself.
     */
    override fun render() {
        this.view.render(GSM.activeGame!!.player.board, *guiObjects.toTypedArray())
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
    }
}
