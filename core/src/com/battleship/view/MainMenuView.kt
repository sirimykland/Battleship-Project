package com.battleship.view

import com.battleship.model.GameObject

class MainMenuView() : View() {

    override fun render(vararg gameObjects: GameObject) {
        batch.begin()
        gameObjects.forEach {
            it.draw(batch)
        }
        batch.end()
    }
}