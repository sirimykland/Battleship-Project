package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.controller.firebase.GameController
import com.battleship.controller.state.deprecated.MenuState
import com.battleship.model.ui.deprecated.Button
import com.battleship.model.ui.deprecated.TextBox
import com.battleship.model.ui.deprecated.TextButton
import com.battleship.view.BasicView
import com.battleship.view.View

class MatchmakingState : MenuState() {
    override var view: View = BasicView()

    private val gameController = GameController()

    val playerButtons: Array<TextButton> = arrayOf(*(0..4).map { a: Int -> joinUserButton(a) }.toTypedArray())

    override val buttons: List<Button> = listOf(
        TextButton(
            20f,
            Gdx.graphics.height - 110f,
            200f,
            90f,
            "<-"
        ) {
            GameStateManager.set(MainMenuState())
        },
        *playerButtons
    )

    var users = emptyMap<String, String>()

    var userList = emptyList<String>()

    private val uiElements = arrayOf(
        *buttons.toTypedArray(),
        TextBox(
            20f,
            Gdx.graphics.height - 220f,
            Gdx.graphics.width - 40f,
            90f,
            "Matchmaking"
        )
    )

    override fun create() {
        super.create()
        users = gameController.getPendingGames()
        userList = users.toList().map { a -> a.second }
        updateButtons(0)
    }

    fun updateButtons(index: Int) {
        userList.subList(index, if (index + 5> userList.size) userList.size else index).forEachIndexed { i, user ->
            playerButtons[i % 5].text = user
        }
    }

    fun joinUserButton(index: Int): TextButton {
        return TextButton(
            50f, Gdx.graphics.height - 330f - index * 110f,
            Gdx.graphics.width - 100f, 90f, "Loading"
        ) {
            println(userList[index])
        }
    }

    override fun update(dt: Float) {
    }

    override fun render() {
        this.view.render(*uiElements)
    }
}
