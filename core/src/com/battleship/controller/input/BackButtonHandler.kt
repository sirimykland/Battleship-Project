package com.battleship.controller.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter

class BackButtonHandler(val onBack: () -> Unit) : InputAdapter() {
    init {
        Gdx.input.setCatchKey(Keys.BACK, true)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.BACK) {
            onBack()
            return true
        }
        return false
    }
}
