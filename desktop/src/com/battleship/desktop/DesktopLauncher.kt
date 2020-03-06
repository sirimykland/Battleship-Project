package com.battleship.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.battleship.BattleshipGame
import com.battleship.controller.firebase.GameController
import com.battleship.controller.firebase.PlayerController


object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val playerController = PlayerController()
        val gameController = GameController()
        val testGame = "MBoFsW2zk0BzyYcFuSpR"
        val testUser = "mPADOxLSsFj3BEwgbXPQ"

        //gameController.addGameListener(testGame,testUser)
        println(gameController.getPendingGames())

        val config = LwjglApplicationConfiguration()
        LwjglApplication(BattleshipGame(), config)
    }
}
