package com.battleship

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidFirebase.createGame("defwgrhty")
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(BattleshipGame(AndroidFirebase), config)
    }
}
