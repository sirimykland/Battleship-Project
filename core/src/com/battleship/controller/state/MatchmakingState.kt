package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

class MatchmakingState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = BasicView()
    private val itemsPerPage = 7
    private var page: Int = 0

    private val playerButtons: Array<GuiObject> =
        arrayOf(*(0 until itemsPerPage).map { a: Int -> joinUserButton(a) }.toTypedArray())

    private val nextPageButton = GUI.textButton(
        70f,
        19f,
        20f,
        5f,
        "Next"
    ) {
        page++
        updateButtons()
    }

    private val previousPageButton = GUI.textButton(
        10f,
        19f,
        20f,
        5f,
        "Previous"
    ) {
        page--
        updateButtons()
    }.hide()

    private val createGameButton = GUI.textButton(
        10f,
        2f,
        80f,
        10f,
        "Create new game"
    ) {
        createGame()
    }

    private val header = GUI.header("Choose your opponent ${GSM.username}")

    override val guiObjects: List<GuiObject> = listOf(
        header,
        nextPageButton,
        previousPageButton,
        *playerButtons,
        createGameButton,
        GUI.backButton { GSM.set(NameSelectionState(controller)) }
    )

    override fun create() {
        super.create()
        controller.addPendingGamesListener { pendingGames ->
            GSM.pendingGames = pendingGames
            updateButtons()
        }
    }

    private fun updateButtons() {
        val index = page * itemsPerPage
        playerButtons.forEachIndexed { i, guiObject ->
            val j = index + i
            if (j < GSM.pendingGames.size) {
                guiObject.set(
                    Text(
                        "Join ${GSM.pendingGames[j].playerName}'s game",
                        font = Font.MEDIUM_BLACK
                    )
                )
                guiObject.show()
            } else {
                guiObject.hide()
            }
        }

        if (index + itemsPerPage < GSM.pendingGames.size) nextPageButton.show() else nextPageButton.hide()
        if (index > 0) previousPageButton.show() else previousPageButton.hide()
    }

    private fun joinUserButton(index: Int): GuiObject {
        return GUI.textButton(
            10f,
            75f - index * 8f,
            80f,
            6f,
            "Loading user"
        ) {
            val i = (page * itemsPerPage) + index
            if (GSM.userId !== "") {
                controller.joinGame(GSM.pendingGames[i].gameId, GSM.userId, GSM.username) { game ->
                    if (game != null) {
                        GSM.activeGame = game
                        GSM.set(PreGameState(controller))
                    } else {
                        GSM.resetGame()
                        GSM.set(MainMenuState(controller))
                    }
                }
            }
        }
    }

    private fun createGame() {
        controller.createGame(GSM.userId, GSM.username) { game ->
            if (game != null) {
                GSM.activeGame = game
                GSM.set(PreGameState(controller))
            } else {
                GSM.resetGame()
                GSM.set(MainMenuState(controller))
            }
        }
    }

    override fun update(dt: Float) {}

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }
}
