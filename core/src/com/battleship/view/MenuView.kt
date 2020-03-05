package com.battleship.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.battleship.BattleshipGame
import com.battleship.model.GameObject
import javax.xml.soap.Text

class MenuView() : View() {

    private val playBtn: Texture
    private val background: Texture

    init {
        cam.setToOrtho(false, BattleshipGame.WIDTH.toFloat(), BattleshipGame.HEIGHT.toFloat())
        background = Texture("menu_background.png")
        playBtn = Texture("menu_button.png")
    }

    //Use game object instead of spritebatch
    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = cam.combined
        sb.begin()
        sb.draw(background, 0f, 0f, BattleshipGame.WIDTH.toFloat(), BattleshipGame.HEIGHT.toFloat())
        sb.draw(playBtn, (BattleshipGame.WIDTH / 2 - playBtn.width / 2).toFloat(), (BattleshipGame.HEIGHT / 2).toFloat())
        sb.end()
    }
}