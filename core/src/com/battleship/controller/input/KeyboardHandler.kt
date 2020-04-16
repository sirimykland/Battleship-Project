package com.battleship.controller.input

import com.badlogic.gdx.InputAdapter

class KeyboardHandler(val onClick: (c: Char) -> Unit) : InputAdapter() {
    override fun keyTyped(character: Char): Boolean {
        onClick(character)
        return true
    }
}
