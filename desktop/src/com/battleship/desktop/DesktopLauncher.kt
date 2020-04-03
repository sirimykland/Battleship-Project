package com.battleship.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.battleship.BattleshipGame


object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val testUser = "LPuSVUECEJMWVcJSEOMc"
        val desktopController = DesktopFirebase()
        println("game: " + desktopController.createGame(testUser))

        val config = LwjglApplicationConfiguration()
        config.height = 800
        config.width = 640
        LwjglApplication(BattleshipGame(desktopController), config)
    }
}
