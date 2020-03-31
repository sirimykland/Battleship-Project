package com.battleship.controller.state

import com.battleship.GameStateManager
import com.battleship.controller.firebase.GameController
import com.battleship.model.ui.GuiObject
import com.battleship.model.ui.Text
import com.battleship.utility.GUI
import com.battleship.view.BasicView
import com.battleship.view.View

class MatchmakingState : GuiState() {
    override var view: View = BasicView()
    private val itemsPerPage = 3

    private val gameController = GameController()

    private val playerButtons: Array<GuiObject> = arrayOf(*(0 until itemsPerPage).map { a: Int -> joinUserButton(a) }.toTypedArray())

    private var page: Int = 0

    init {

        // gameController.createGame("NefN8HJT5S90ZmDwFGco")
    }

    private val nextPageButton = GUI.textButton(
        78f,
        2f,
        20f,
        10f,
        "->"
    ) {
        page++
        updateButtons()
    }

    private val previousPageButton = GUI.textButton(
        2f,
        2f,
        20f,
        10f,
        "<-"
    ) {
        page--
        updateButtons()
    }.hide()

    override val guiObjects: List<GuiObject> = listOf(
        GUI.header("Matchmaking"),
        nextPageButton,
        previousPageButton,
        *playerButtons,
        GUI.backButton { GameStateManager.set(MainMenuState()) }
    )

    var users = emptyMap<String, String>()

    var userList = emptyList<String>()

    override fun create() {
        super.create()
        retrieve(this::updateButtons)
    }

    private fun retrieve(callback: () -> Unit) {
        users = gameController.getPendingGames()
        userList = users.toList().map { a -> a.second }
        callback()
    }

    private fun updateButtons() {
        val index = page * itemsPerPage
        playerButtons.forEachIndexed { i, guiObject ->
            val j = index + i
            if (j < userList.size) {
                guiObject.set(Text(userList[j]))
                guiObject.show()
            } else {
                guiObject.hide()
            }
        }
        if (index + itemsPerPage < userList.size)
            nextPageButton.show()
        else
            nextPageButton.hide()

        if (index> 0)
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
            println(userList[(page * 5) + index])
        }
    }

    override fun update(dt: Float) {
    }

    override fun render() {
        this.view.render(*guiObjects.toTypedArray())
    }
}
