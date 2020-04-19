package com.battleship

import com.battleship.controller.state.State
import com.battleship.model.Game
import com.battleship.model.GameListObject
import java.util.Stack

object GameStateManager {
    var username = ""
    var userId = getRandomString(15)
    var activeGame: Game? = null
    var pendingGames = ArrayList<GameListObject>()
    var treasuresInitialized: Boolean = false

    private val states: Stack<State> = Stack()

    private fun getRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun push(state: State) {
        if (states.size > 0)
            states.peek().pause()
        states.push(state)
        create()
    }

    fun pop() {
        states.pop().dispose()
        if (states.size > 0)
            states.peek().resume()
    }

    fun set(state: State) {
        states.pop().dispose()
        states.push(state)
        create()
    }

    fun create() {
        states.peek().create()
    }

    fun update(dt: Float) {
        states.peek().update(dt)
    }

    fun render() {
        states.peek().render()
    }
}
typealias GSM = GameStateManager
