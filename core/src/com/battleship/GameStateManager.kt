package com.battleship

import com.battleship.controller.state.State
import java.util.Stack

object GameStateManager {

    private val states: Stack<State>

    init {
        states = Stack<State>()
    }
    fun push(state: State) {
        states.push(state)
        create()
    }
    fun pop() {
        states.pop()
    }
    fun set(state: State) {
        states.pop()
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
