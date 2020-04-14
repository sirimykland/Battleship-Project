package com.battleship.model.treasures
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

class GoldBar(position: Vector2, rotate: Boolean) : Treasure(position) {
    override var dimension: Vector2 = Vector2(1f, 3f)
    override var name: String = "Shiny bar of gold"
    override var health: Int = 3
    override var sound: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/goldbar_sound.mp3"))
    override var sprite: Sprite = Sprite(Texture("images/goldbar.png"))
    override var type: TreasureType = TreasureType.GOLDBAR

    init {
        if (rotate) rotateTreasure()
    }
}
