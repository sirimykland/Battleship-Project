package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.battleship.GameStateManager
import com.battleship.controller.firebase.FindPlayer
import com.battleship.controller.firebase.FirebaseController
import com.battleship.model.ui.Button
import com.battleship.model.ui.TextBox
import com.battleship.model.ui.TextButton
import com.battleship.view.BasicView
import com.battleship.view.View

class MatchmakingState : MenuState() {
    override var view: View = BasicView()
    override var firebaseController: FirebaseController = FindPlayer()

    val playerButtons: Array<TextButton> = arrayOf(*(0..4).map { a: Int -> joinUserButton(a) }.toTypedArray())

    override val buttons: List<Button> = listOf(
        TextButton(20f, Gdx.graphics.height - 110f, 200f, 90f, "Back") {
            println("as")
            GameStateManager.set(TestMenuState())
        },
        *playerButtons
    )

    var users = emptyMap<String, String>()

    var userList = emptyList<String>()

    private val uiElements = arrayOf(
        *buttons.toTypedArray(),
        TextBox(20f, Gdx.graphics.height - 220f, Gdx.graphics.width - 40f, 90f, "Matchmaking")
    )

    override fun create() {
        super.create()
        users = mapOf(
            Pair("jonas", "a73ab"),
            Pair("bendik", "6b293"),
            Pair("ingrid", "9c99d"),
            Pair("vivian", "ab434"),
            Pair("berek", "8be20")
        )
        userList = users.toList().map { a -> a.first }
        playerButtons.forEachIndexed { i, button ->
            button.text = userList[i]
        }
    }

    fun joinUserButton(index: Int): TextButton {
        return TextButton(
            50f, Gdx.graphics.height - 330f - index * 110f,
            Gdx.graphics.width - 100f, 90f, "Loading") {
                println(userList[index])
        }
    }

    override fun update(dt: Float) {
    }

    override fun render() {
        this.view.render(*uiElements)
    }
}
