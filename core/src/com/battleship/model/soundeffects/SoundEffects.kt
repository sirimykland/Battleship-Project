package com.battleship.model.soundeffects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.battleship.BattleshipGame

/**
 * Class for manging sound effects
 */
class SoundEffects {

    private var hit: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/hit_sound.mp3"))
    private var victory: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/victory_sound.mp3"))
    private var losing: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/losing_sound.mp3"))
    private var click: Sound = Gdx.audio.newSound(Gdx.files.internal("audio/click_sound.mp3"))

    /**
     * Triggers sound to play on Hit
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playHit(volume: Float) {
        if (BattleshipGame.soundOn) {
            hit.stop()
            hit.play(volume)
        }
    }

    /**
     * Triggers sound to play on click
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playClick(volume: Float) {
        if (BattleshipGame.soundOn) {
            click.stop()
            click.play(volume)
        }
    }

    /**
     * Triggers sound to play for victory
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playVictory(volume: Float) {
        if (BattleshipGame.soundOn) {
            victory.play(volume)
        }
    }

    /**
     * Triggers sound to play sound for loosing
     *
     * @param volume: Float - the volume of the sound played
     */
    fun playLosing(volume: Float) {
        if (BattleshipGame.soundOn) {
            losing.play(volume)
        }
    }
}
