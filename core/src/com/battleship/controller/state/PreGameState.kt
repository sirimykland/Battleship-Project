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

    override fun create() {
        super.create()
        println("---PREGAMESTATE---")
        if (!GSM.treasuresInitialized) {
            GSM.activeGame!!.me.board.createAndPlaceTreasures(1, Treasure.TreasureType.GOLDKEY, true)
            GSM.activeGame!!.me.board.createAndPlaceTreasures(1, Treasure.TreasureType.GOLDCOIN, true)
            GSM.activeGame!!.me.board.createAndPlaceTreasures(1, Treasure.TreasureType.TREASURECHEST, true)
            controller.addGameListener(GSM.activeGame!!.gameId, GSM.activeGame!!.me.playerId)
            GSM.treasuresInitialized = true
        }
    }

    private val readyButton = GUI.textButton(
            5f,
            3f,
            90f,
            10f,
            "Start Game",
            onClick = {
                val game = GSM.activeGame!!
                println("gameready is: " + game.gameReady)
                println("t: " + game.player.board.getTreasuresList())
                controller.registerTreasures(
                        game.gameId,
                        game.player.playerId,
                        game.player.board.getTreasuresList()
                )
                GSM.set(LoadingGameState(controller))
            })

    var guiTing: GuiObject = GuiObject(0f, 0f, 0f, 0f)

    override val guiObjects: List<GuiObject> = listOf(
        readyButton,
        GUI.header("Place treasures"),
        GUI.backButton { GSM.set(MainMenuState(controller)) },
        GuiObject(0f, 0f, 0f, 0f)
            .listen(TreasureHandler(GSM.activeGame!!.player.board))
    )

    override fun render() {
        this.view.render(*guiObjects.toTypedArray(), GSM.activeGame!!.player.board)
    }

    override fun update(dt: Float) {

    }


}
