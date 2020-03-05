package com.battleship.view

import com.badlogic.gdx.graphics.Texture
import com.battleship.BattleshipGame

class MainMenuView() : View() {

    var playBtn: Texture = Texture("menu_button.png")
    var background: Texture = Texture("menu_background.png")


    //Use game object instead of spritebatch
    override fun render() {
        batch.begin()
        batch.draw(background, 0f, 0f, BattleshipGame.WIDTH.toFloat(), BattleshipGame.HEIGHT.toFloat())
        batch.draw(playBtn, (BattleshipGame.WIDTH / 2 - playBtn.width / 2).toFloat(), (BattleshipGame.HEIGHT / 2).toFloat())
        batch.end()
    }
}