package com.battleship

import com.battleship.controller.state.State
import com.battleship.model.Game
import java.util.*

object GameStateManager {
    val userId: String = "LPuSVUECEJMWVcJSEOMc" // må ha login/bruker registrering og lagre den her
    lateinit var activeGame: Game

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