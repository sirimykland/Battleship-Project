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
        val testGame2 = "z0yfbbCxghe4YKz7gnvd"
        val testUser2 = "Skytern"

        val testShips = mutableListOf<Map<String, Any>>()
        val ship1 = mutableMapOf<String, Any>()
        val ship2 = mutableMapOf<String, Any>()
        ship1["type"] = "Smallboii"
        ship1["x"] = 4
        ship1["y"] = 121
        ship2["type"] = "BigBoi"
        ship2["x"] = 99
        ship2["y"] = 33
        testShips.add(ship1)
        testShips.add(ship2)
        gameController.addGameListener(testGame2,testUser2)

        // gameController.addGameListener(testGame,testUser)
        println(gameController.getPendingGames())

        val config = LwjglApplicationConfiguration()
        config.height = 800
        LwjglApplication(BattleshipGame(), config)
    }
}
