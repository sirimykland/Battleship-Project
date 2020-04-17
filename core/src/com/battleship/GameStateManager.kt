package com.battleship

import com.battleship.controller.state.State
import com.battleship.model.Game
import com.battleship.model.GameListObject
import java.util.*
import kotlin.collections.ArrayList

object GameStateManager {
    var username = ""
    var userId = ""
    var activeGame: Game? = null
    var pendingGames = ArrayList<GameListObject>()
    var first: Boolean = true

    private val states: Stack<State> = Stack()

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
