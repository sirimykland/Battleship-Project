package com.battleship.model.treasures

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.battleship.utility.TextureLibrary

class GoldCoin(position: Vector2, rotate: Boolean) : Treasure(position, rotate) {
    override var dimension: Vector2 = Vector2(1f, 1f)
    override var name: String = "Gold coin"
    override var health: Int = 1
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/coin_sound.mp3"))
    override var sprite: Sprite = Sprite(TextureLibrary.GOLDCOIN)
    override var type: TreasureType = TreasureType.GOLDCOIN

    init {
        if (rotate) rotateTreasure()
    }
}
