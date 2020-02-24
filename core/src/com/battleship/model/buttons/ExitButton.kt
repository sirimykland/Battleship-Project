package com.battleship.model.buttons

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.InputEvent


class ExitButton : Button() {
    override fun draw(batch: SpriteBatch) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val btn: TextButton
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun addListener() {
        btn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })
    }
}