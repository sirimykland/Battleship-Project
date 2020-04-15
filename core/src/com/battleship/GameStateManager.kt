package com.battleship

import com.battleship.controller.state.State
import com.battleship.model.Game
import com.battleship.model.GameListObject
import java.util.Stack
import kotlin.collections.ArrayList

object GameStateManager {
    var userId: String = "LPuSVUECEJMWVcJSEOMc"
    var activeGame: Game? = null
    var pendingGames = ArrayList<GameListObject>()

    private val states: Stack<State> = Stack()

    fun push(state: State) {
        states.push(state)
        create()
    }

    fun pop() {
        states.pop().dispose()
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
