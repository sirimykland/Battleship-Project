package com.battleship.controller.state

import com.battleship.GSM
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View
import java.lang.IllegalStateException

class MatchmakingState(private val controller: FirebaseController) : GuiState(controller) {
    override var view: View = BasicView()
    private val itemsPerPage = 5
    var games = GSM.pendingGames

    private val playerButtons: Array<GuiObject> =
        arrayOf(*(0 until itemsPerPage).map { a: Int -> joinUserButton(a) }.toTypedArray())

    private var page: Int = 0

    private val nextPageButton = GUI.textButton(
        76f,
        14f,
        22f,
        5f,
        "Next",
        font = Font.SMALL_BLACK
    ) {
        page++
        updateButtons()
    }

    private val previousPageButton = GUI.textButton(
        2f,
        14f,
        22f,
        5f,
        "Previous",
        font = Font.SMALL_BLACK
    ) {
        page--
        updateButtons()
    }.hide()

    private val usernameElement = if (GSM.username == "")
        GUI.textButton(
            10f,
            5f,
            40f,
            7f,
            "Select username",
            font = Font.TINY_BLACK,
            color = Palette.WHITE,
            borderColor = Palette.BLACK
        ) {
            GSM.push(NameSelectionState(controller))
        }
    else
        GUI.textBox(
            5f,
            5f,
            40f,
            7f,
            GSM.username,
            font = Font.TINY_BLACK,
            color = Palette.WHITE,
            borderColor = Palette.BLACK
        )

    private val createGameButton = GUI.textButton(
        55f,
        5f,
        40f,
        7f,
        "Create game",
        color = Palette.GREEN,
        font = Font.TINY_BLACK
    ) {
        createGame()
        GSM.set(PreGameState(controller))
    }.hide()

    init {
        if (GSM.username !== "")
            createGameButton.show()
    }

    private val refreshButton = GUI.imageButton(
        80f,
        78f,
        10f,
        10f,
        "icons/refresh_black.png",
        keepRatio = true
    ) {
        updateButtons()
    }
        .with(Background(color = Palette.WHITE))
        .with(Border(color = Palette.BLACK, width = 3f))

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Matchmaking"),
        nextPageButton,
        previousPageButton,
        *playerButtons,
        usernameElement,
        createGameButton,
        refreshButton,
        GUI.backButton { GSM.set(MainMenuState(controller)) }
    )

    override fun create() {
        super.create()
        updateButtons()
    }

    override fun resume() {
        if (GSM.username !== "") {
            createGameButton.show()
            usernameElement
                .set(Text(GSM.username, Font.TINY_BLACK))
                .noClick()
        }
        super.resume()
    }

    private fun updateButtons() {
        controller.getPendingGames()
        games = GSM.pendingGames
        val index = page * itemsPerPage
        playerButtons.forEachIndexed { i, guiObject ->
            val j = index + i
            if (j < games.size) {
                guiObject.set(Text(games[j].playerName, font = Font.MEDIUM_BLACK))
                guiObject.show()
            } else {
                guiObject.hide()
            }
        }
        println(playerButtons)
        println(games)
        if (index + itemsPerPage < games.size)
            nextPageButton.show()
        else
            nextPageButton.hide()

        if (index > 0)
            previousPageButton.show()
        else
            previousPageButton.hide()
    }

    private fun joinUserButton(index: Int): GuiObject {
        println("userbutton $index")
        return GUI.textButton(
            10f,
            70f - index * 9f,
            80f,
            7f,
            "Loading"
        ) {
            val i = (page * itemsPerPage) + index
            if (GSM.userId !== "") {
                controller.joinGame(GSM.pendingGames[i].gameId, GSM.userId)
            }
        }
    }

    private fun createGame() {
        if (GSM.userId !== "")
            controller.createGame(GSM.userId)
        else
            throw IllegalStateException("Can't create game, user has not been created")
    }

    override fun update(dt: Float) {
        if (!games.containsAll(GSM.pendingGames)) { // O(n²) hver frame er kanskje mye å be om?
            games = GSM.pendingGames
            updateButtons()
        }
        if (GSM.activeGame != null) {
            print(GSM.activeGame!!.gameId)
            if(GSM.activeGame!!.playersRegistered()) {
                GSM.set(PreGameState(controller))
            }
        }
        if (GSM.userId != "") {
            createGameButton.show()
        }
    }

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }
}
