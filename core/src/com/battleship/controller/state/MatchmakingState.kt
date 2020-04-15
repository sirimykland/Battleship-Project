package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.controller.firebase.GameController
import com.battleship.model.ui.Background
import com.battleship.model.ui.Border
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Gamelist
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View
import java.lang.IllegalStateException

class MatchmakingState : GuiState() {
    override var view: View = BasicView()
    private val itemsPerPage = 6

    private val gameController = GameController()

    private val playerButtons: Array<GuiObject> =
        arrayOf(*(0 until itemsPerPage).map { a: Int -> joinUserButton(a) }.toTypedArray())

    private var page: Int = 0

    private var gameId = ""

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

    private val usernameElement = if (GameStateManager.username == "")
        GUI.textButton(
            10f,
            5f,
            40f,
            7f,
            "Select username",
            font = Font.SMALL_BLACK,
            color = Palette.WHITE,
            borderColor = Palette.BLACK
        ) {
            GameStateManager.push(NameSelectionState())
        }
    else
        GUI.textBox(
            5f,
            5f,
            40f,
            7f,
            GameStateManager.username,
            font = Font.SMALL_BLACK,
            color = Palette.WHITE,
            borderColor = Palette.BLACK
        )

    private val createGameButton = GUI.textButton(
        55f,
        5f,
        40f,
        7f,
        "Create game",
        color = Palette.GREEN
    ) {
        createGame()
        retrieve(this::updateButtons)
    }.hide()

    init {
        if (GameStateManager.username !== "")
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
        retrieve(this::updateButtons)
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
        GUI.backButton { GameStateManager.set(MainMenuState()) }
    )

    private var userList: Gamelist = emptyList()

    override fun create() {
        super.create()
        retrieve(this::updateButtons)
    }

    override fun resume() {
        if (GameStateManager.username !== "") {
            createGameButton.show()
            usernameElement
                .set(Text(GameStateManager.username, Font.SMALL_BLACK))
                .noClick()
        }
        super.resume()
    }

    private fun retrieve(callback: () -> Unit) {
        userList = gameController.getPendingGames().toList()
        callback()
    }

    private fun updateButtons() {
        if (userList.find { it.first == gameId } !== null) {
            println("found me")
            createGameButton.hide()
        }
        val index = page * itemsPerPage
        playerButtons.forEachIndexed { i, guiObject ->
            val j = index + i
            if (j < userList.size) {
                guiObject.set(Text(userList[j].second, font = Font.MEDIUM_BLACK))
                guiObject.show()
            } else {
                guiObject.hide()
            }
        }
        if (index + itemsPerPage < userList.size)
            nextPageButton.show()
        else
            nextPageButton.hide()

        if (index > 0)
            previousPageButton.show()
        else
            previousPageButton.hide()
    }

    private fun joinUserButton(index: Int): GuiObject {
        return GUI.textButton(
            10f,
            70f - index * 9f,
            80f,
            7f,
            "Loading"
        ) {
            val i = (page * itemsPerPage) + index
            if (GameStateManager.userId !== "" && userList[i].first != gameId) {
                println("${userList[i]}, $gameId")
                // gameController.joinGame(userList[i].first, GameStateManager.userId)
            }
        }
    }

    private fun createGame() {
        if (GameStateManager.userId !== "")
            gameId = gameController.createGame(GameStateManager.userId)
        else
            throw IllegalStateException("Can't create game, user has not been created")
    }

    override fun update(dt: Float) {
    }

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }
}
