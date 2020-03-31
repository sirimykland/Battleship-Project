package com.battleship.controller.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.battleship.model.ui.GuiObject
import com.battleship.utility.Font
import com.battleship.utility.GUI
import com.battleship.utility.Palette
import com.battleship.view.BasicView
import com.battleship.view.View

class FxTestState : GuiState(){

    override var view: View = BasicView()
    var shovel: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/shovel_sound.mp3"))
    var coin: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/coin_sound.mp3"))

    var sounds: List<Sound> = listOf(
        shovel,
        coin
    )

    fun stopSounds(){
        for(sound in sounds){
            sound.stop()
        }
    }



    private var shovelButton: GuiObject = GUI.textButton(
        Gdx.graphics.width / 2 - 170f,
        Gdx.graphics.height - 380f,
        340f,
        140f,
        "Shovel",
        font = Font.MEDIUM_WHITE,
        color = Palette.BLACK,
        borderColor = Palette.BLUE,
        onClick = {
            stopSounds()
            shovel.play(0.5f)}
    )

    private var coinButton: GuiObject = GUI.textButton(
        Gdx.graphics.width / 2 - 170f,
        Gdx.graphics.height - 560f,
        340f,
        140f,
        "Coin",
        font = Font.MEDIUM_WHITE,
        color = Palette.BLACK,
        borderColor = Palette.BLUE,
        onClick = {
            stopSounds()
            coin.play(0.8f)}
    )

    override val guiObjects: List<GuiObject> = listOf(
        shovelButton,
        coinButton,
        GUI.backButton
    )

    override fun create() {
        super.create()
    }


    override fun update(dt: Float) {
    }

    override fun render() {
        view.render(*guiObjects.toTypedArray())
    }
}
