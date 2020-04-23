package com.battleship.utility

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.battleship.BattleshipGame

/**
 * Singleton for manging sound effects
 */
object SoundEffects {

    private val hit: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/hit_sound.mp3"))
    private val victory: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/victory_sound.mp3"))
    private val losing: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/losing_sound.mp3"))
    private val click: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/click_sound.mp3"))

    val METALDETECTOR: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/radar_sound.mp3"))
    val BIGEQUIPMENT: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/bomb_sound.mp3"))
    val SHOVEL: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/dirt_sound.mp3"))

    val GOLDCOIN: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/coin_sound.mp3"))
    val GOLDKEY: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/key_sound.mp3"))
    val TREASURECHEST: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/chest_sound.mp3"))

    /**
     *  Load method
     */
    fun load() {
        println("SFX Loaded")
    }

    /**
     * Triggers sound to play on Hit
     *
     * @param volume: Float - the volume of the sound played: default is 0.6f
     */
    fun playHit(volume: Float = 0.8f) {
        play(hit, volume)
    }

    /**
     * Triggers sound to play on click
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playClick(volume: Float = 4.0f) {
        play(click, volume)
    }

    /**
     * Triggers sound to play for victory
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playVictory(volume: Float = 0.6f) {
        play(victory, volume)
    }

    /**
     * Triggers sound to play sound for loosing
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playLosing(volume: Float = 0.6f) {
        play(losing, volume)
    }

    /**
     * Checks global sound settings to decide whether to play or not
     *
     * @param sound: Sound
     * @param volume: Float
     */
    fun play(sound: Sound, volume: Float) {
        if (BattleshipGame.soundOn) {
            sound.stop()
            sound.play(volume)
        }
    }

    /**
     *  Dispose method: for disposing all sound objects
     */
    fun dispose() {
        listOf(hit, victory, losing, click,
            METALDETECTOR, BIGEQUIPMENT, SHOVEL,
            GOLDCOIN, GOLDKEY, TREASURECHEST).forEach {
            it.dispose()
        }
    }
}
