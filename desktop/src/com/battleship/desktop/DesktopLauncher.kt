package com.battleship.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.battleship.BattleshipGame
import com.battleship.controller.firebase.AuthResponse
import com.battleship.controller.firebase.Authentication
import com.battleship.controller.firebase.GameController
import com.battleship.controller.firebase.PlayerController
import com.google.gson.Gson
import com.google.gson.JsonObject


object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val gameController = GameController()
        val testGame = "MBoFsW2zk0BzyYcFuSpR"
        val testUser = "mPADOxLSsFj3BEwgbXPQ"

        gameController.addGameListener(testGame,testUser)



        val config = LwjglApplicationConfiguration()
        LwjglApplication(BattleshipGame(), config)
    }
}
