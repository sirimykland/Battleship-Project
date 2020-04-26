package com.battleship.controller.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter
/**
 * Back button (Android only) handler class inheriting from [InputAdapter].
 *
 * @constructor
 * @param onBack: () -> Unit - the function that should be invoked when the back button is touched
 */
class BackButtonHandler(val onBack: () -> Unit) : InputAdapter() {
    init {
        Gdx.input.setCatchKey(Keys.BACK, true)
    }
    /**
     * Called when a key was pressed
     * If the key pressed was [Keys.BACK], [onBack] is invoked
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.BACK) {
            onBack()
            return true
        }
        return false
    }
}
