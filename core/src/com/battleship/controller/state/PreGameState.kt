package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.controller.input.TreasureHandler
import com.battleship.model.treasures.Treasure
import com.battleship.model.ui.GuiObject
import com.battleship.utility.GUI
import com.battleship.view.PlayView
import com.battleship.view.View

class PreGameState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = PlayView()
    var showDialog: Boolean = false
    var opponentLeftRenders: Int = 0

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
        "${GSM.activeGame!!.opponent.playerName} left the game.",
        listOf(Pair("Find new game", {
            showDialog = false
            leaveGame()
        }))
    )

    override val guiObjects: List<GuiObject> = listOf(
        readyButton,
        GUI.header("Place treasures"),
        GUI.backButton { leaveGame() },
        GuiObject(0f, 0f, 0f, 0f)
            .listen(TreasureHandler(GSM.activeGame!!.player.board)),
        *opponentLeftDialog
    )

    private fun leaveGame() {
        controller.leaveGame(GSM.activeGame!!.gameId, GSM.userId) {
            GSM.resetGame()
            controller.addPendingGamesListener { pendingGames ->
                GSM.pendingGames = pendingGames
            }
            GSM.set(MainMenuState(controller))
        }
    }

    override fun render() {
        this.view.render(GSM.activeGame!!.player.board, *guiObjects.toTypedArray())
    }

    override fun update(dt: Float) {
        if (GSM.activeGame!!.opponentLeft) {
            if (opponentLeftRenders < 2) opponentLeftRenders++
            if (opponentLeftRenders == 1) showDialog = true // First opponent left render
        }

        if (showDialog) opponentLeftDialog.forEach { guiObject -> guiObject.show() }
        else opponentLeftDialog.forEach { guiObject -> guiObject.hide() }
    }
}
