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
    var games = GSM.pendingGames

    private val playerButtons: Array<GuiObject> =
        arrayOf(*(0 until itemsPerPage).map { a: Int -> joinUserButton(a) }.toTypedArray())

    private var page: Int = 0

    // temp
    private var selectUsernameCallback = false

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
        // temporarily removed for bugfix in android
        // GSM.set(PreGameState(controller))
    }

    private val refreshButton = GUI.imageButton(
        87f,
        90f,
        8f,
        8f,
        "icons/refresh_white.png",
        onClick = { updateButtons() }
    )
    private val header = GUI.header("Choose your opponent ${GSM.username}")

    override val guiObjects: List<GuiObject> = listOf(
        header,
        nextPageButton,
        previousPageButton,
        *playerButtons,
        createGameButton,
        refreshButton,
        GUI.backButton { GSM.set(NameSelectionState(controller)) }
    )

    override fun create() {
        super.create()
        updateButtons()
    }

    private fun updateButtons() {
        controller.getPendingGames()
        games = GSM.pendingGames
        print(games)
        val index = page * itemsPerPage
        var loading = true
        playerButtons.forEachIndexed { i, guiObject ->
            val j = index + i
            if (j < games.size) {
                guiObject.set(Text("Join ${games[j].playerName}'s game", font = Font.MEDIUM_BLACK))
                loading = false
                guiObject.show()
            } else {
                guiObject.hide()
            }
        }

        if (loading) header.set(Text("Loading available opponents...", font = Font.MEDIUM_WHITE))
        else header.set(Text("Choose your opponent ${GSM.username}", font = Font.MEDIUM_WHITE))

        if (index + itemsPerPage < games.size) nextPageButton.show() else nextPageButton.hide()
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
            if (GSM.userId !== "") controller.joinGame(GSM.pendingGames[i].gameId, GSM.userId)
        }
    }

    private fun createGame() {
        if (GSM.userId !== "") controller.createGame(GSM.userId)
        else print("Can't create game, user has not been created")
    }

    override fun update(dt: Float) {
        if (!games.containsAll(GSM.pendingGames)) { // O(n²) hver frame er kanskje mye å be om?
            games = GSM.pendingGames
            updateButtons()
        }
        if (GSM.activeGame != null) GSM.set(PreGameState(controller))
        if (GSM.username != "" && !selectUsernameCallback) createGameButton.show()
    }

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }
}
