package com.battleship.controller.input

import com.badlogic.gdx.InputAdapter

/**
 * Click handler class inheriting from [InputAdapter].
 *
 * @constructor
 * @property onClick: (c: Char) -> Unit
 */
class KeyboardHandler(val onClick: (c: Char) -> Unit) : InputAdapter() {

    /**
     * Called when a key was typed
     *
     * @param character: Char
     * @return: Boolean
     */
    override fun keyTyped(character: Char): Boolean {
        onClick(character)
        return true
    }
}
