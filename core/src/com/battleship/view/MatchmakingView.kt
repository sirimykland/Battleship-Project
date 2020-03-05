package com.battleship.view

import com.battleship.model.GameObject

class MatchmakingView : View() {
    override fun render(gameObject: GameObject) {
        batch.begin()
        gameObject.draw(batch)
        batch.end()
    }
}
